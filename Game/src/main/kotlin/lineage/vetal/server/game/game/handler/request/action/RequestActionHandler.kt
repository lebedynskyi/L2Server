package lineage.vetal.server.game.game.handler.request.action

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.action.usecase.InteractUseCase
import lineage.vetal.server.game.game.handler.request.action.validation.InteractionValidation
import lineage.vetal.server.game.game.handler.request.action.usecase.SelectTargetSuccessUseCase
import lineage.vetal.server.game.game.handler.request.action.validation.SelectTargetValidation
import lineage.vetal.server.game.game.model.behaviour.data.TargetData
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.validation.onError
import lineage.vetal.server.game.game.validation.onSuccess
import lineage.vetal.server.game.gameserver.packet.server.TargetUnSelected

private const val TAG = "RequestActionHandler"

class RequestActionHandler(
    private val context: GameContext,
    private val interactionValidation: InteractionValidation = InteractionValidation(),
    private val selectTargetValidation: SelectTargetValidation = SelectTargetValidation(),
    private val interactUseCase: InteractUseCase = InteractUseCase(),
    private val selectTargetSuccessUseCase: SelectTargetSuccessUseCase = SelectTargetSuccessUseCase(),
) {
    fun onPlayerAction(player: PlayerObject, objectId: Int) {
        val item = player.region.getVisibleItem(objectId)
        if (item != null) {
            context.requestItemHandler.onPlayerPickUpItem(player, item)
            return
        }

        val actionTarget = player.region.getVisibleNpc(objectId) ?: player.region.getVisiblePlayer(objectId)
        onPlayerAction(player, actionTarget)
    }

    fun onPlayerAction(player: PlayerObject, actionTarget: CreatureObject?) {
        if (actionTarget?.objectId == player.target?.objectId) {
            interactionValidation.validate(player, actionTarget)
                .onSuccess {
                    if (it.isAutoAttackable) {
                        context.attackManager.startAttackTask(player, it)
                    } else {
                        Intention.INTERACT(TargetData(it, context.clock.millis()))
                    }
                }.onError {
                    interactUseCase.onInteractionError(context, player, it)
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
        if (player.target != null) {
            player.target = null
            player.sendPacket(TargetUnSelected(player.objectId, player.position.x, player.position.y, player.position.z))
        }
    }
}