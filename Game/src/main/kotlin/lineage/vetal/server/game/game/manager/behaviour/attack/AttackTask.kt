package lineage.vetal.server.game.game.manager.behaviour.attack

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.manager.behaviour.BehaviourTask
import lineage.vetal.server.game.game.model.behaviour.BehaviourResult
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject

class AttackTask(
    private val context: GameContext,
    private val attackUseCase: BehaviourAttackUseCase,
    private val attack: Intention.ATTACK,
    creature: CreatureObject,
) : BehaviourTask(creature) {
    override suspend fun step(): BehaviourResult {
        if (!isStillAttacking(creature, attack)) {
            return BehaviourResult.INTERRUPTED
        }

        attackUseCase.resolveHit(context, creature, attack)

        return if (isStillAttacking(creature, attack)) {
            BehaviourResult.IN_PROGRESS
        } else {
            BehaviourResult.FINISHED
        }
    }

    override fun nextDelay(): Long {
        return attackUseCase.calculateHitDelay(creature).toLong()
    }

    private fun isStillAttacking(creature: CreatureObject, attack: Intention.ATTACK): Boolean {
        return creature.behaviour.action === attack
    }
}
