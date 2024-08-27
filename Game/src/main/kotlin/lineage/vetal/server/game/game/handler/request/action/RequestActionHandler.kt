package lineage.vetal.server.game.game.handler.request.action

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.action.usecase.InteractFailUseCase
import lineage.vetal.server.game.game.handler.request.action.usecase.InteractSuccessUseCase
import lineage.vetal.server.game.game.handler.request.action.validation.InteractionValidation
import lineage.vetal.server.game.game.handler.request.action.usecase.SelectTargetSuccessUseCase
import lineage.vetal.server.game.game.handler.request.action.validation.SelectTargetValidation
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onSuccess
import lineage.vetal.server.game.gameserver.packet.server.TargetUnSelected

private const val TAG = "RequestActionHandler"

class RequestActionHandler(
    private val context: GameContext,
    private val interactionValidation: InteractionValidation = InteractionValidation(),
    private val selectTargetValidation: SelectTargetValidation = SelectTargetValidation(),
    private val interactFailUseCase: InteractFailUseCase = InteractFailUseCase(),
    private val interactSuccessUseCase: InteractSuccessUseCase = InteractSuccessUseCase(),
    private val selectTargetSuccessUseCase: SelectTargetSuccessUseCase = SelectTargetSuccessUseCase(),
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
            interactionValidation.validate(player, actionTarget)
                .onSuccess {
                    interactSuccessUseCase.onInteractionSuccess(context, player, it)
                }.onError {
                    interactFailUseCase.onInteractionError(context, player, it)
                }
        } else {
            selectTargetValidation.validate(player, actionTarget)
                .onSuccess {
                    selectTargetSuccessUseCase.onSelectTargetSuccess(player, it)
                }
        }
    }

    fun onPlayerCancelAction(player: PlayerObject, unselect: Int) {
        // TODO check unselect and check cast. trade and etc
        player.target = null
        player.sendPacket(TargetUnSelected(player.objectId, player.position.x, player.position.y, player.position.z))
    }
}