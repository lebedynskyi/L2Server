## Context

Two action mechanisms exist today, each completing/cancelling independently:

- **Movement** (`MovementManagerOld` extends `BehaviourManagerOld`, a `TickTask`): polls every 100ms, `handleBehaviour(creature, behaviour): Boolean` returns `true` when the loop should drop the creature from `activeCreatures`. The shared `onTick` then *unconditionally* calls `behaviour.endCurrent()` whenever `handleBehaviour` returns `true` — whether that's because movement finished naturally or because something else already swapped `behaviour.current` away from `MOVE_TO`. This is the active bug: `endCurrent()` advances `behaviour.next → current`, which is only correct in the "finished naturally" case.
- **Attack** (`AttackManager` + `AttackHitTask`, from the `event-driven-attack-timer` change): self-rescheduling via `ScheduleTaskManager`. `AttackManager` owns a `ConcurrentHashMap<Int, Job>` keyed by creature id and exposes `cancelCurrentAttack(creature)`. Other managers cancel attack by calling this directly — `MovementManagerOld.startMovement` does so at `MovementManagerOld.kt:16`.

This pairwise wiring (`movement → attack.cancelCurrentAttack`) doesn't scale: adding skill/cast means skill needs to cancel both movement and attack, and movement/attack need to cancel skill — up to N×(N-1) hand-written calls for N action types, easy to miss one.

`BehaviourManager` already exists as an empty stub (`BehaviourManager.kt`) extending `ScheduleTaskManager` — this change gives it a real job: single front door for starting, cancelling, and advancing any creature action.

## Goals / Non-Goals

**Goals:**
- One outcome type (`BehaviourResult`) shared by both the tick-based path (movement) and the schedule-based path (attack, future skill/cast), so "finished naturally" and "got interrupted" are never conflated.
- One place (`BehaviourManager.advanceQueue`) that calls `behaviour.endCurrent()` — and only in response to `FINISHED`.
- One cancellation table shared by every schedule-based action, so starting any new action automatically cancels whatever schedule-based action preceded it — no pairwise manager-to-manager calls.
- Fold `AttackManager`'s scheduling responsibility into `BehaviourManager`; keep `BehaviourAttackUseCase` as the pure calculation layer (delay, hit resolution, target validity) wrapped by a thin `AttackTask : BehaviourTask`.

**Non-Goals:**
- Skill/cast implementation itself — only the contract (`BehaviourTask`, `BehaviourResult`) it will plug into.
- Changing movement's internal interpolation/position-broadcast logic (`BehaviourMoveToUseCase`) — only its `handleBehaviour` return type and who calls `endCurrent()`.
- Revisiting whether movement should become schedule-based instead of tick-based (explored, deferred — separate future change).

## Decisions

### 1. `BehaviourResult` enum shared across tick and schedule paths
`IN_PROGRESS | FINISHED | INTERRUPTED`. Both `MovementManagerOld.handleBehaviour` and `BehaviourTask.step()` (attack, future skill) return this instead of `Boolean`. `BehaviourManager` reacts identically regardless of mechanism:

```
IN_PROGRESS  → tick: stay in activeCreatures / schedule: reschedule via task.nextDelay()
FINISHED     → remove from tracking + advanceQueue(creature)   // endCurrent(), only here
INTERRUPTED  → remove from tracking, nothing else              // interrupter already owns current/next
```

Alternative considered: keep `Boolean` and add a separate `wasInterrupted` flag/out-param. Rejected — two independent booleans re-creates the same conflation risk (forgetting to check one), whereas a 3-state enum makes the cases mutually exclusive by construction.

### 2. `BehaviourTask` abstract class centralizes reschedule plumbing
```kotlin
abstract class BehaviourTask(val creature: CreatureObject) {
    abstract suspend fun step(): BehaviourResult
    abstract fun nextDelay(): Long   // consulted only when step() == IN_PROGRESS
}
```
`BehaviourManager` wraps any `BehaviourTask` in one internal `ScheduleTask` that calls `step()`, reschedules itself via `nextDelay()` on `IN_PROGRESS`, and otherwise removes the creature's job entry and calls `advanceQueue` only on `FINISHED`. Today's `AttackHitTask` duplicates this reschedule logic itself (`manager.scheduleHit(...)` call inside `execute()`) — that duplication goes away once every schedule-based action shares the same wrapper. `AttackTask : BehaviourTask` becomes a thin adapter: `step()` delegates to `BehaviourAttackUseCase.isStillAttacking`/`resolveHit`, `nextDelay()` delegates to `calculateHitDelay`.

### 3. Single cancellation table lives on `BehaviourManager`, not per-action-manager
`BehaviourManager` owns `ConcurrentHashMap<Int, Job>` (the table `AttackManager` owns today). `BehaviourManager.start(creature, intention, task, delay)` always cancels the creature's existing entry first, then sets intention, then schedules. Movement doesn't have a `Job` (it's tick-based, no `ScheduleTaskManager` involvement) — its "cancellation" is `movementManager.removeCreature(creature)`, which `BehaviourManager.cancelCurrent(creature)` also performs unconditionally (a no-op if the creature wasn't in `activeCreatures`). This means `BehaviourManager` needs a reference to `MovementManagerOld` to call `removeCreature` — acceptable, since `BehaviourManager` is explicitly the coordinator that's allowed to know about every action mechanism; that knowledge living in one place is the point.

Alternative considered: give every action manager (movement, attack, future skill) a common `Cancellable` interface and have `BehaviourManager` hold a generic `List<Cancellable>`, calling `cancel(creature)` on all of them. Rejected as unnecessary indirection for three concrete, known mechanisms (tick-based movement, job-based schedule actions) — a generic interface would be justified only if the cancellation *mechanism* itself varied per action type, but movement and "everything schedule-based" are the only two mechanisms that exist or are planned.

### 4. Handlers call `BehaviourManager`, not per-action managers, not `behaviour.setIntention` directly
`InteractUseCase.kt:21` (`context.attackManager.onCreatureAttack(...)`) and movement's call sites move to `context.behaviourManager.start(...)` / `context.behaviourManager.startMovement(...)`. This is what makes the centralized cancellation table actually effective — if any call site still reaches around `BehaviourManager` straight into `creature.behaviour.setIntention`, the old pairwise-cancellation bug reappears for that path.

## Risks / Trade-offs

- **[Risk]** `BehaviourManager` becomes a god-object that knows about every action mechanism (movement's tick set, attack/skill's job map). → **Mitigation**: it's deliberately the *only* place allowed to know this — the alternative (each manager knowing about every other manager) is the problem being fixed. Keep per-action domain logic (`BehaviourAttackUseCase`, `BehaviourMoveToUseCase`) fully ignorant of scheduling/cancellation; `BehaviourManager` only orchestrates.
- **[Risk]** Existing call sites that bypass `BehaviourManager` (if any are missed during migration) silently reintroduce the pairwise-cancellation bug for that path. → **Mitigation**: task list includes an explicit grep/audit step for any remaining direct `behaviour.setIntention`/`attackManager.`/`movementManager.startMovement` call sites outside `BehaviourManager`.
- **[Risk]** `advanceQueue` must be the *only* caller of `endCurrent()` — if any leftover code path calls `endCurrent()` directly, the original bug returns. → **Mitigation**: task list includes removing/deleting `BehaviourManagerOld`'s direct `endCurrent()` call once `BehaviourManager` owns it.

## Migration Plan

Code-only change, no data migration:
1. Add `BehaviourResult` enum and `BehaviourTask` abstract class (additive, no callers yet).
2. Implement `BehaviourManager`: cancellation table, `start`, `cancelCurrent`, `advanceQueue`, `startMovement`.
3. Change `MovementManagerOld.handleBehaviour` to return `BehaviourResult`; remove its loop's direct `endCurrent()` call (moves to `BehaviourManager.advanceQueue`, invoked from the tick loop only on `FINISHED`).
4. Replace `AttackManager`/`AttackHitTask` with `AttackTask : BehaviourTask`; repoint `InteractUseCase` to `behaviourManager.start(...)`.
5. Repoint movement's start call sites to `behaviourManager.startMovement(...)`.
6. Remove `AttackManager.kt`, `AttackHitTask.kt`; remove `attackManager` from `GameContext`.
Rollback: revert steps 4-6 (restore `AttackManager`) since steps 1-3 are additive/inert without callers using them.

## Open Questions

- Should `BehaviourManager.startMovement` live alongside `start()` as a separate method (different mechanism, tick vs schedule), or should movement eventually be expressed as a `BehaviourTask` too (ties back to the deferred "movement as scheduled" exploration)? Leaning: keep separate for now, since movement's tick loop also drives interest-management broadcast — folding it into `BehaviourTask` prematurely conflates two different future changes.
- Exact `MovementManagerOld` retirement/rename (does `BehaviourManagerOld` get deleted once movement no longer needs its own `TickTask` base, or does movement keep its own minimal tick loop separate from `BehaviourManagerOld`?) — implementation-level, deferred to tasks.
