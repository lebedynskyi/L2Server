package lineage.vetal.server.game.game.handler.request.action.usecase

import lineage.vetal.server.game.game.handler.request.action.validation.SelectTargetValidationError
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.server.StatusAttr
import lineage.vetal.server.game.gameserver.packet.server.StatusUpdate
import lineage.vetal.server.game.gameserver.packet.server.TargetSelected

class SelectTargetSuccessUseCase {
    internal fun onSelectTargetSuccess(player: PlayerObject, target: CreatureObject) {
        player.target = target

        val playerPos = player.position
        val targetSelection = TargetSelected(player.objectId, target.objectId, playerPos.x, playerPos.y, playerPos.z)
        val targetStatus = StatusUpdate(
            target.objectId,
            listOf(
                StatusAttr.curHp(target.stats.curHp.toInt()),
                StatusAttr.maxHp(target.stats.getMaxHp().toInt())
            )
        )

        player.sendPacket(targetSelection, targetStatus)
    }

    internal fun onSelectTargetFail(player: PlayerObject, reason: SelectTargetValidationError) {

    }
}
