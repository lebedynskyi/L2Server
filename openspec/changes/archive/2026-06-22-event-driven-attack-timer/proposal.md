## Why

`AttackManager` drives combat timing off a fixed 100ms poll (`DEFAULT_ATTACK_TICK_PERIOD` in `GameContext.kt`), checking `attackData.nextTime` once per tick. This quantizes both the delay before the first hit and the gap between successive hits to the tick grid, introducing up to ±100ms of jitter on attack start and on cooldown expiry. The same poll-based pattern will repeat for skill/cast timing unless the underlying mechanism changes now. Movement is unaffected by this change — it needs per-tick interpolation/broadcast regardless of timing precision, so it keeps polling.

## What Changes

- Fix `ScheduleTaskManager.schedule()`: honor the `delay` parameter (currently ignored, tasks fire immediately) and return the launched `Job` (currently discarded, no cancellation possible).
- Add a generic `currentJob: Job?` slot to `GameObjectBehaviour`, cancelled in `setIntention()`/`endCurrent()` before any intention swap — a single cancellation hook usable by attack now and skill/cast later.
- Rewrite attack timing to be self-rescheduling: starting an attack schedules one delayed hit via `ScheduleTaskManager`; each executed hit checks whether the creature is still attacking the same target, computes the next delay from live attack speed, and reschedules itself, storing the returned `Job` on `behaviour.currentJob`. **BREAKING**: `AttackManager` no longer drives attacks through `BehaviourManager`'s tick-poll loop for the `ATTACK` intention.
- Decide and document combat dispatcher confinement: per-creature self-rescheduling jobs on `Dispatchers.IO` introduce real parallelism that today's single tick-loop `forEach` accidentally avoids (all attacking creatures currently resolve on one thread per tick).
- Movement stays exactly as-is (`MovementManager`, tick-polled) — explicitly out of scope.

## Capabilities

### New Capabilities
- `combat-timing`: event-driven, jitter-free scheduling of attack hits (start delay and inter-hit cooldown), replacing tick-poll-based timing. Covers the scheduling/cancellation contract intended to be reused by future skill/cast timing.

### Modified Capabilities
(none — no pre-existing specs in `openspec/specs/`)

## Impact

- `Game/src/main/kotlin/lineage/vetal/server/game/game/task/ScheduleTaskManager.kt` — delay/Job-return fix.
- `Game/src/main/kotlin/lineage/vetal/server/game/game/model/behaviour/GameObjectBehaviour.kt` — new `currentJob` field + cancellation in `setIntention`/`endCurrent`.
- `Game/src/main/kotlin/lineage/vetal/server/game/game/manager/behaviour/attack/AttackManager.kt` and `BehaviourAttackUseCase.kt` — rewritten to self-reschedule instead of being polled by `BehaviourManager.onTick`.
- `Game/src/main/kotlin/lineage/vetal/server/game/game/GameContext.kt` — combat dispatcher wiring (exact shape depends on the confinement decision in design.md).
- No client-facing protocol change; no DB schema change.
