package lineage.vetal.server.game.game.manager.behaviour.attack

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.manager.behaviour.BehaviourManager
import lineage.vetal.server.game.game.model.behaviour.CreatureBehaviour
import lineage.vetal.server.game.game.model.behaviour.data.AttackData
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject

class AttackManager(
    private val context: GameContext,
    private val attackUseCase: BehaviourAttackUseCase = BehaviourAttackUseCase()
) : BehaviourManager(context) {
    fun startAttackTask(player: PlayerObject, actionTarget: CreatureObject) {
        val attackData = AttackData(actionTarget)
        val newIntent = Intention.ATTACK(attackData)
        player.behaviour.setIntention(newIntent)
        manageCreature(player)
    }

    override fun handleBehaviour(creature: CreatureObject, behaviour: CreatureBehaviour): Boolean {
        val currentIntention = behaviour.current
        return if (currentIntention is Intention.ATTACK) {
            attackUseCase.onBehaviourAttack(context, creature, currentIntention)
        } else {
            true
        }
    }
}