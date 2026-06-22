## Context

`AttackManager` (`Game/.../manager/behaviour/attack/AttackManager.kt`) extends `BehaviourManager` (`Game/.../manager/behaviour/BehaviourManager.kt`), which is a `TickTask` polled by `TickTaskManager` every `DEFAULT_ATTACK_TICK_PERIOD` (100ms, `GameContext.kt`). Each tick iterates all attacking creatures and calls `BehaviourAttackUseCase.onBehaviourAttack`, which checks `attackData.nextTime` against the clock and either resolves a hit or no-ops until the next tick. This quantizes hit timing to the 100ms tick grid.

`TickTaskManager` and `ScheduleTaskManager` both run on `Dispatchers.IO` (`GameContext.kt:131-133`), a multi-threaded pool. Today, `AttackManager`'s tick body runs as a single coroutine per tick, so the `forEach` over attacking creatures is incidentally serialized onto one thread per tick — no two attacking creatures' hits resolve concurrently right now.

`ScheduleTaskManager.schedule(task, delay)` exists but is currently unused, ignores `delay` (executes immediately), and discards the launched `Job` (no cancellation possible). `GameObjectBehaviour` (`Game/.../model/behaviour/GameObjectBehaviour.kt`) holds `current`/`next` `Intention` and swaps them in `setIntention`/`endCurrent`, with no notion of an associated running job today.

## Goals / Non-Goals

**Goals:**
- Eliminate tick-grid jitter from attack start delay and inter-hit cooldown.
- Make `ScheduleTaskManager` correct (honor delay, expose cancellation) and reusable for skill/cast timing later.
- Provide one generic cancellation hook on `GameObjectBehaviour` so any future intention type (skill, cast) can plug into the same interrupt path without bespoke per-intention wiring.
- Make an explicit, documented decision about combat-thread confinement rather than leaving it to incidental tick-loop serialization.

**Non-Goals:**
- Movement timing — stays tick-polled via `MovementManager`, unchanged.
- Skill/cast implementation itself — only the reusable scaffolding (`ScheduleTaskManager` fix, `currentJob` slot) is built now.
- Combat formulas (damage, crit, hit chance) — unchanged, out of scope.

## Decisions

### 1. Self-rescheduling task instead of manager-polled loop
Replace the tick-poll with a one-shot `ScheduleTask` that, on execution, resolves the hit, decides whether to continue (target still valid, intention still `ATTACK` on the same target), computes the next delay from current attack speed, and reschedules itself via `ScheduleTaskManager.schedule(this, nextDelay)`. `AttackManager` stops registering with `TickTaskManager`; it becomes a thin entry point that starts the first scheduled task on `onCreatureAttack`.

Alternative considered: keep the tick loop but shrink `DEFAULT_ATTACK_TICK_PERIOD` (e.g. to 20ms) to reduce jitter. Rejected — still has a jitter floor, still polls every creature every tick regardless of whether it's due, and doesn't fix the underlying pattern before skill/cast needs the same mechanism.

### 2. Job storage: manager-side `ConcurrentHashMap<Int, Job>`, not on `GameObjectBehaviour` or `AttackData`
Revisited after initial implementation. A generic `currentJob: Job?` on `GameObjectBehaviour` was tried first (automatic cancel-on-swap inside `setIntention`/`endCurrent`), but rejected in favor of keeping the domain model (`GameObjectBehaviour`) free of scheduling concerns — `Job`/coroutines are an execution-mechanism detail, not part of what an intention *is*. `AttackManager` now owns a `ConcurrentHashMap<Int, Job>` keyed by creature `objectId`, populated/cleared by `scheduleHit`/`cancelAttack`.

Trade-off accepted: this state can drift from `behaviour.current` if some other code path swaps intention away from `ATTACK` without going through `AttackManager`. Mitigated by giving `AttackManager` a public `cancelAttack(creature)` that other managers call before swapping intention away from `ATTACK` — `MovementManager.startMovement` calls it today. Any future caller that interrupts an attack (skill cast, stun, death, disconnect) must do the same; this is a manual convention, not enforced by the type system.

### 3. `ScheduleTaskManager` fix is a correctness fix, not a redesign
`schedule()` keeps its current signature/shape but: (a) actually delays before executing (`delay(delay)` inside the launched coroutine), (b) returns the `Job` from `scope.launch`. No new abstraction introduced — this is the minimal fix needed to build on it.

### 4. Combat dispatcher confinement: single-threaded combat dispatcher
Per-creature self-rescheduling jobs on `Dispatchers.IO` would let two attacking creatures' hits resolve on different threads simultaneously — a real change from today's accidental single-thread-per-tick serialization. Given `CreatureObject`/HP/intention fields are plain mutable fields with no synchronization, true parallel mutation is unsafe.

Decision: give combat its own `ScheduleTaskManager` instance backed by `Dispatchers.IO.limitedParallelism(1)` (or an equivalent single-threaded dispatcher), passed into `AttackManager`/`BehaviourAttackUseCase`. This preserves today's effective serialization guarantee while still removing tick-grid jitter (delay is per-task, not per-poll-cycle). Movement keeps its own dispatcher/manager, unaffected.

Alternatives considered:
- Per-creature actor/mutex: gives true parallelism across creatures, but adds non-trivial complexity (lock acquisition order across attacker/target pairs) for a problem not yet proven to be a bottleneck. Deferred — revisit only if single-threaded combat dispatcher becomes a measured throughput limit.
- Accept races, guard only hot fields with atomics: rejected, since correctness of combat state (intention swaps, HP, death) is harder to reason about with fine-grained atomics than with a single confinement thread.

## Risks / Trade-offs

- **[Risk]** Single-threaded combat dispatcher caps combat throughput to one hit-resolution at a time server-wide. → **Mitigation**: revisit only if profiling shows this is an actual bottleneck; cheap to swap for per-creature confinement later since the dispatcher is encapsulated behind `ScheduleTaskManager`.
- **[Risk]** Self-rescheduling tasks can outlive their intent if cancellation is missed on an edge case (disconnect, death mid-flight, region change). → **Mitigation**: `currentJob` cancellation is centralized in `setIntention`/`endCurrent`, plus the task's own continue-check (still attacking same target) acts as a second independent stop condition.
- **[Risk]** `ScheduleTaskManager`'s fixed signature change (`schedule` now returns `Job`) has zero current callers, so no migration risk — confirmed via codebase search before writing this design.

## Migration Plan

No data migration. Deployment is a code-only change:
1. Land `ScheduleTaskManager` fix (additive correctness fix, no callers yet — safe in isolation).
2. Land `GameObjectBehaviour.currentJob` + cancellation in `setIntention`/`endCurrent` (no-op until something sets it).
3. Land combat dispatcher wiring in `GameContext.kt`.
4. Rewrite `AttackManager`/`BehaviourAttackUseCase` to self-reschedule, remove it from `TickTaskManager` registration.
Rollback: revert step 4 (restore tick-poll registration) since steps 1-3 are additive/inert without it.

## Open Questions

- Should the single-threaded combat dispatcher be shared with future skill/cast timing, or does each get its own single-threaded dispatcher? (Leaning: share one "combat" dispatcher for attack+skill+cast, since they mutate the same creature state and the goal is serializing combat-state mutation, not separating attack from skill.)
- Exact backoff/continue-check details (e.g. range check, target-death check) for the self-rescheduling task are implementation-level and deferred to tasks/code, not blocking this design.
