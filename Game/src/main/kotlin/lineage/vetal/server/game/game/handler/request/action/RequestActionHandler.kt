package lineage.vetal.server.game.game.handler.request.action

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.action.usecase.InteractUseCase
import lineage.vetal.server.game.game.handler.request.action.validation.InteractionValidation
import lineage.vetal.server.game.game.handler.request.action.usecase.SelectTargetSuccessUseCase
import lineage.vetal.server.game.game.handler.request.action.validation.SelectTargetValidation
import lineage.vetal.server.game.game.handler.request.item.usecase.PlayerPickItemUseCase
import lineage.vetal.server.game.game.handler.request.item.validation.PickUpValidation
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onValid
import lineage.vetal.server.game.gameserver.packet.server.TargetUnSelected

private const val TAG = "RequestActionHandler"

class RequestActionHandler(
    private val context: GameContext,
    private val interactionValidation: InteractionValidation = InteractionValidation(),
    private val selectTargetValidation: SelectTargetValidation = SelectTargetValidation(),
    private val pickUpValidation: PickUpValidation = PickUpValidation(),
    private val interactUseCase: InteractUseCase = InteractUseCase(context),
    private val selectTargetSuccessUseCase: SelectTargetSuccessUseCase = SelectTargetSuccessUseCase(),
    private val playerPickItemUseCase: PlayerPickItemUseCase = PlayerPickItemUseCase(context),
) {
    fun onRequestAction(player: PlayerObject, objectId: Int) {
        val item = player.region.getVisibleItem(objectId)
        if (item != null) {
            onRequestPickUp(player, item)
        } else {
            onPlayerSelect(player, objectId)
        }
    }

    fun onRequestPickUp(player: PlayerObject, item: ItemObject) {
        pickUpValidation.validate(player, item)
            .onValid {
                playerPickItemUseCase.onPlayerPickUpItemSuccess(context, player, item)
            }.onError {
                playerPickItemUseCase.onPlayerPickItemFailed(it, player)
            }
    }

    fun onRequestCancelAction(player: PlayerObject, unselect: Int) {
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

    private fun onPlayerSelect(player: PlayerObject, objectId: Int) {
        val actionTarget = player.region.getVisibleNpc(objectId) ?: player.region.getVisiblePlayer(objectId)

        if (actionTarget?.objectId == player.target?.objectId) {
            interactionValidation.validate(player, actionTarget)
                .onValid {
                    interactUseCase.onInteractionSuccess(player, it)
                }.onError {
                    interactUseCase.onInteractionError(player, it)
                }
        } else {
            selectTargetValidation.validate(player, actionTarget)
                .onValid {
                    selectTargetSuccessUseCase.onSelectTargetSuccess(player, it)
                }.onError {
                    selectTargetSuccessUseCase.onSelectTargetFail(player, it)
                }
        }
    }
}
