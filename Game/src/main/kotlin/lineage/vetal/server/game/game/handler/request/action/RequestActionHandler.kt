package lineage.vetal.server.game.game.handler.request.action

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.action.usecase.InteractUseCase
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
    private val interactUseCase: InteractUseCase = InteractUseCase(context),
    private val selectTargetSuccessUseCase: SelectTargetSuccessUseCase = SelectTargetSuccessUseCase(),
) {
    fun onPlayerAction(player: PlayerObject, objectId: Int) {
        val item = player.region.getVisibleItem(objectId)
        if (item != null) {
            context.requestItemHandler.onPlayerPickUpItem(player, item)
        } else {
            val actionTarget = player.region.getVisibleNpc(objectId) ?: player.region.getVisiblePlayer(objectId)

            if (actionTarget?.objectId == player.target?.objectId) {
                interactionValidation.validate(player, actionTarget)
                    .onSuccess {
                        interactUseCase.onInteractionSuccess(player, it)
                    }.onError {
                        interactUseCase.onInteractionError(player, it)
                    }
            } else {
                selectTargetValidation.validate(player, actionTarget)
                    .onSuccess {
                        selectTargetSuccessUseCase.onSelectTargetSuccess(player, it)
                    }.onError {
                        selectTargetSuccessUseCase.onSelectTargetFail(player, it)
                    }
            }
        }
    }

    fun onPlayerCancelAction(player: PlayerObject, unselect: Int) {
        // TODO check unselect and check cast. trade and etc
        if (player.target != null) {
            player.target = null
            player.sendPacket(
                TargetUnSelected(
                    player.objectId,
                    player.position.x,
                    player.position.y,
                    player.position.z
                )
            )
        }
    }
}