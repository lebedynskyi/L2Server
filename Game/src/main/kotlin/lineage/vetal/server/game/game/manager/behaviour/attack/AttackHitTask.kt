package lineage.vetal.server.game.game.manager.behaviour.attack

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.task.ScheduleTask
import java.time.Clock

class AttackHitTask(
    private val context: GameContext,
    private val attackUseCase: BehaviourAttackUseCase,
    private val creature: CreatureObject,
    private val attack: Intention.ATTACK,
    private val manager: AttackManager
) : ScheduleTask() {
    override suspend fun execute(clock: Clock) {
        if (!attackUseCase.isStillAttacking(creature, attack)) {
            return
        }

        attackUseCase.resolveHit(context, creature, attack)

        if (!attackUseCase.isStillAttacking(creature, attack)) {
            return
        }

        val delay = attackUseCase.calculateHitDelay(creature).toLong()
        manager.scheduleHit(creature, attack, delay)
    }
}
