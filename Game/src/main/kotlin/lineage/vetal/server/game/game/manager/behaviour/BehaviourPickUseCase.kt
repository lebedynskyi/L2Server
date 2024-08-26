package lineage.vetal.server.game.game.manager.behaviour

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.PlayerObject

object BehaviourPickUseCase {
    fun onBehaviourPick(context: GameContext, creature: PlayerObject, currentIntention: Intention.PICK): Boolean {
        context.requestItemHandler.onPlayerPickUpItem(creature, currentIntention.itemObject)
        return true
    }
}