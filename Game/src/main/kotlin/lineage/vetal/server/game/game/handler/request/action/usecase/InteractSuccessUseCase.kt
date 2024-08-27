package lineage.vetal.server.game.game.handler.request.action.usecase

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.behaviour.data.AttackData
import lineage.vetal.server.game.game.model.behaviour.data.TargetData
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject

class InteractSuccessUseCase {
    fun onInteractionSuccess(context: GameContext, player: PlayerObject, actionTarget: CreatureObject) {
        val intention = if (actionTarget.isAutoAttackable) {
            Intention.ATTACK(AttackData(actionTarget, context.clock.millis()))
        } else {
            Intention.INTERACT(TargetData(actionTarget, context.clock.millis()))
        }

        context.behaviourManager.onPlayerIntention(player, intention)
    }
}