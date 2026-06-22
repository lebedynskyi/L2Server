## Why

`BehaviourManagerOld`'s tick loop conflates "this creature's action finished naturally" with "this creature's intention was hijacked by something else" — both cases make `handleBehaviour` return `true`, and the loop reacts to both by calling `behaviour.endCurrent()`, which can advance a queued `next` intention that belongs to whatever interrupted it, not to the manager that returned `true`. Separately, cross-actor cancellation (movement stopping attack, and soon skill needing to stop both) is wired pairwise (`MovementManagerOld.startMovement` calling `context.attackManager.cancelCurrentAttack` directly) — each new action type multiplies the number of manual cancel calls needed. Adding skill casting now, before fixing this, means writing N² wiring by hand instead of once.

## What Changes

- Introduce `BehaviourResult` (`IN_PROGRESS` / `FINISHED` / `INTERRUPTED`) as the single outcome type for both the tick-based path (movement) and the schedule-based path (attack, future skill/cast) — replacing the ambiguous `Boolean` return of `handleBehaviour`.
- Introduce `BehaviourTask`, an abstract class capturing the domain-step contract (`step(): BehaviourResult`, `nextDelay(): Long`) for schedule-based actions, so reschedule/cancel plumbing lives in one place instead of being duplicated per action (today only in `AttackHitTask`).
- `BehaviourManager` (currently a stub extending `ScheduleTaskManager`) becomes the single front door for starting/cancelling/advancing any creature action: owns the shared `Job` cancellation table, owns `advanceQueue` (the only place `behaviour.endCurrent()` is called, and only on `FINISHED`), and is the entry point handlers call instead of going directly to per-action managers or `creature.behaviour.setIntention`.
- **BREAKING**: `AttackManager` is folded into this — its scheduling/cancellation responsibilities move to `BehaviourManager`; `BehaviourAttackUseCase` keeps the pure combat calculation (delay, hit resolution, target validity) but is wrapped by an `AttackTask : BehaviourTask` instead of being driven by `AttackManager`/`AttackHitTask` directly.
- `MovementManagerOld.handleBehaviour` changes its return type from `Boolean` to `BehaviourResult`; its tick loop stops calling `endCurrent()` itself — that responsibility moves to `BehaviourManager.advanceQueue`, invoked only on `FINISHED`.
- Handlers/use-cases that currently call `attackManager.onCreatureAttack` or `movementManager.startMovement` directly are repointed to call through `BehaviourManager`.

## Capabilities

### New Capabilities
- `behaviour-coordination`: the single start/cancel/advance contract (`BehaviourResult`, `BehaviourTask`, `BehaviourManager`) that all creature actions (movement, attack, future skill/cast) go through, replacing ad-hoc pairwise cancellation and the ambiguous tick-loop completion signal.

### Modified Capabilities
- `combat-timing`: the "Attack scheduling is cancellable via a single handle" requirement changes mechanism — cancellation is no longer a manager-to-manager call (`cancelCurrentAttack`) but routes through the shared `BehaviourManager` cancellation table used by all action types.

## Impact

- `Game/src/main/kotlin/lineage/vetal/server/game/game/manager/behaviour/BehaviourManager.kt` — from stub to the real coordinator.
- `Game/src/main/kotlin/lineage/vetal/server/game/game/manager/behaviour/BehaviourManagerOld.kt` — retired or absorbed; `handleBehaviour`/`onTick` logic relocates with the `BehaviourResult` contract.
- `Game/src/main/kotlin/lineage/vetal/server/game/game/manager/behaviour/movement/MovementManagerOld.kt` — `handleBehaviour` signature change, drop direct `cancelCurrentAttack` call (superseded by centralized cancellation).
- `Game/src/main/kotlin/lineage/vetal/server/game/game/manager/behaviour/attack/AttackManager.kt`, `AttackHitTask.kt` — removed/replaced by `AttackTask : BehaviourTask`.
- `Game/src/main/kotlin/lineage/vetal/server/game/game/manager/behaviour/attack/BehaviourAttackUseCase.kt` — unchanged in spirit (pure calculation), call sites change.
- `Game/src/main/kotlin/lineage/vetal/server/game/game/GameContext.kt` — wiring updates (drop `attackManager`, wire the real `BehaviourManager`).
- `Game/src/main/kotlin/lineage/vetal/server/game/game/handler/request/action/usecase/InteractUseCase.kt` — call site repointed from `attackManager.onCreatureAttack` to `behaviourManager.start(...)`.
- No client-facing protocol change; no DB schema change. Skill/cast itself stays out of scope — this lays the contract it will plug into.
