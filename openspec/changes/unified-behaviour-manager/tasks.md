## 1. BehaviourResult and BehaviourTask

- [x] 1.1 Add `enum class BehaviourResult { IN_PROGRESS, FINISHED, INTERRUPTED }`
- [x] 1.2 Add `abstract class BehaviourTask(val creature: CreatureObject)` with `abstract suspend fun step(): BehaviourResult` and `abstract fun nextDelay(): Long`

## 2. BehaviourManager as the real coordinator

- [x] 2.1 Add `ConcurrentHashMap<Int, Job>` to `BehaviourManager` for schedule-based actions
- [x] 2.2 Implement `BehaviourManager.start(creature, intention, task: BehaviourTask, delay)`: cancel previous action, set intention, schedule a wrapper task
- [x] 2.3 Implement the internal wrapper `ScheduleTask` that calls `task.step()`, reschedules via `task.nextDelay()` on `IN_PROGRESS`, removes the job entry on `FINISHED`/`INTERRUPTED`, and calls `advanceQueue(creature)` only on `FINISHED`
- [x] 2.4 Implement `BehaviourManager.cancelCurrent(creature)`: cancel+remove the job entry AND remove the creature from movement's active set (covers both schedule-based and tick-based previous actions)
- [x] 2.5 Implement `BehaviourManager.advanceQueue(creature)`: the one place that calls `behaviour.endCurrent()`
- [x] 2.6 Implement `BehaviourManager.startMovement(creature, destination, intention?)`: cancel previous action via `cancelCurrent`, set `MOVE_TO` intention, hand off to `MovementManagerOld.manageCreature`

## 3. Fix MovementManagerOld's completion signal

- [x] 3.1 Change `MovementManagerOld.handleBehaviour` return type from `Boolean` to `BehaviourResult`: `INTERRUPTED` when `current !is MOVE_TO`, `FINISHED` when destination reached, `IN_PROGRESS` otherwise
- [x] 3.2 Update `BehaviourManagerOld.onTick` (or move this loop into `BehaviourManager`) to call `advanceQueue` only when `handleBehaviour` returns `FINISHED`, never on `INTERRUPTED`
- [x] 3.3 Remove the direct `behaviour.endCurrent()` call from the tick loop now that `BehaviourManager.advanceQueue` owns it
- [x] 3.4 (follow-up) Merge `BehaviourManagerOld` + `MovementManagerOld` into one `MovementManager` (extends `TickTask` directly) since movement was `BehaviourManagerOld`'s only subclass — no abstract base needed anymore

## 4. Fold AttackManager into BehaviourTask

- [x] 4.1 Create `AttackTask : BehaviourTask` wrapping `BehaviourAttackUseCase`: `step()` returns `INTERRUPTED` if not still attacking the same target, else resolves the hit and returns `FINISHED`/`IN_PROGRESS` based on target validity; `nextDelay()` delegates to `calculateHitDelay`
- [x] 4.2 Remove `AttackManager.kt` and `AttackHitTask.kt`
- [x] 4.3 Remove `attackManager` from `GameContext`; ensure `BehaviourManager` is wired with whatever `BehaviourAttackUseCase` needs

## 5. Repoint call sites through BehaviourManager

- [x] 5.1 Update `InteractUseCase.kt:21` (`context.attackManager.onCreatureAttack`) to call `context.behaviourManager.start(player, Intention.ATTACK(...), AttackTask(...), delay)`
- [x] 5.2 Update movement's start call sites to `context.behaviourManager.startMovement(...)` instead of `context.movementManager.startMovement(...)` directly
- [x] 5.3 Remove `MovementManagerOld.startMovement`'s direct `context.attackManager.cancelCurrentAttack(creature)` call (superseded by centralized cancellation in `BehaviourManager.startMovement`)
- [x] 5.4 Grep for any remaining direct calls to `creature.behaviour.setIntention`/`endCurrent` outside `BehaviourManager` and repoint or justify each one

## 6. Verification

- [x] 6.1 Compile check (`./gradlew :Game:compileKotlin`)
- [ ] 6.2 Manual test: start moving, then attack mid-movement — movement stops, attack proceeds, no stale `next` advancement
- [ ] 6.3 Manual test: start attacking, then move mid-attack — attack's pending hit is cancelled, no hit fires after movement starts
- [ ] 6.4 Manual test: attack runs to natural completion (target dies/leaves range) with a queued `next` intention — confirm `next` becomes `current` correctly via `advanceQueue`
