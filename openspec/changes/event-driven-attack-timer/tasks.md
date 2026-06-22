## 1. Fix ScheduleTaskManager

- [x] 1.1 In `ScheduleTaskManager.schedule(task, delay)`, call `delay(delay)` inside the launched coroutine before `task.execute(clock)`
- [x] 1.2 Change `schedule()` to return the `Job` from `scope.launch` instead of discarding it

## 2. Cancellation hook for attack jobs

- [x] 2.1 Add `ConcurrentHashMap<Int, Job>` to `AttackManager` keyed by creature `objectId` (revised from initial `GameObjectBehaviour.currentJob` approach — see design.md decision 2)
- [x] 2.2 Add `AttackManager.cancelAttack(creature)` removing+cancelling the job for that creature
- [x] 2.3 Call `cancelAttack` from `MovementManager.startMovement` before swapping intention away from `ATTACK`

## 3. Combat dispatcher wiring

- [x] 3.1 In `GameContext.kt`, create a single-threaded combat dispatcher (e.g. `Dispatchers.IO.limitedParallelism(1)`)
- [x] 3.2 Construct a dedicated `ScheduleTaskManager(clock, combatDispatcher)` instance for combat scheduling
- [x] 3.3 Wire the combat `ScheduleTaskManager` into `AttackManager`'s constructor in place of/alongside its current `BehaviourManager` wiring

## 4. Self-rescheduling attack task

- [x] 4.1 Define the attack hit task (e.g. as a `ScheduleTask` subtype or equivalent) holding attacker, target, and attack use-case reference
- [x] 4.2 Implement its `execute()`: resolve the hit via `BehaviourAttackUseCase`, then check whether the creature is still attacking the same target
- [x] 4.3 If still attacking the same target: compute next delay from current attack speed and call `AttackManager.scheduleHit`, which schedules via `scheduleTaskManager` and stores the returned `Job` in `AttackManager`'s job map
- [x] 4.4 If not still attacking: do not reschedule (let the chain end)
- [x] 4.5 Update `AttackManager.onCreatureAttack` to set the `ATTACK` intention and kick off the first scheduled hit task (initial delay) instead of calling `manageCreature`

## 5. Remove tick-poll path for attack

- [x] 5.1 Remove `attackManager` registration from `TickTaskManager` in `GameContext.kt` (drop `register(attackManager, DEFAULT_ATTACK_TICK_PERIOD)`)
- [x] 5.2 Remove or repurpose `AttackManager`'s `BehaviourManager`/`handleBehaviour` override now that hits are no longer polled
- [x] 5.3 Confirm `BehaviourAttackUseCase.onBehaviourAttack`'s tick-driven early-return (`time < nextTime` check) is no longer needed and remove or adapt it for the new single-shot call shape

## 6. Verification

- [ ] 6.1 Manual test: start an attack, confirm hits land at the expected cadence without visible tick-aligned stutter
- [ ] 6.2 Manual test: interrupt an in-progress attack (e.g. change intention/target) and confirm no further hits occur from the old target
- [ ] 6.3 Manual test: two creatures attacking simultaneously resolve correctly with no corrupted/lost state (sanity check for the single-threaded combat dispatcher decision)
