package lineage.vetal.server.game.game.handler.request.action

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.action.usecase.InteractFailUseCase
import lineage.vetal.server.game.game.handler.request.action.validation.InteractionValidation
import lineage.vetal.server.game.game.handler.request.action.usecase.SelectTargetSuccessUseCase
import lineage.vetal.server.game.game.handler.request.action.validation.SelectTargetValidation
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onSuccess
import lineage.vetal.server.game.gameserver.packet.server.TargetUnSelected

private const val TAG = "ActionManager"

class RequestActionHandler(
    private val context: GameContext
) {
    fun onPlayerAction(player: PlayerObject, objectId: Int) {
        val item = player.region.getVisibleItem(objectId)
        if (item != null) {
            context.requestItemHandler.onPlayerPickUpItem(player, item)
            return
        }

        val actionTarget = player.region.getVisibleNpc(objectId) ?: player.region.getVisiblePlayer(objectId)
        if (actionTarget?.objectId == player.target?.objectId) {
            // interact with creature
            InteractionValidation.validate(player, actionTarget)
                .onSuccess {
                    InteractFailUseCase.onInteractionSuccess(context, player, it)
                }.onError {
                    InteractFailUseCase.onInteractionError(context, player, it)
                }
        } else {
            SelectTargetValidation.validate(player, actionTarget)
                .onSuccess {
                    SelectTargetSuccessUseCase.onSelectTargetSuccess(player, it)
                }
        }
    }

    fun onPlayerCancelAction(player: PlayerObject, unselect: Int) {
        // TODO check unselect and check cast. trade and etc
        player.target = null
        player.sendPacket(TargetUnSelected(player.objectId, player.position.x, player.position.y, player.position.z))
    }
}