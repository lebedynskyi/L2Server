package lineage.vetal.server.game.game.handler.request.action.usecase

import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.server.StatusAttr
import lineage.vetal.server.game.gameserver.packet.server.StatusUpdate
import lineage.vetal.server.game.gameserver.packet.server.TargetSelected

class SelectTargetSuccessUseCase {
    fun onSelectTargetSuccess(player: PlayerObject, target: CreatureObject) {
        val playerPos = player.position
        player.target = target

        val targetSelection = TargetSelected(player.objectId, target.objectId, playerPos.x, playerPos.y, playerPos.z)
        val targetStatus = StatusUpdate(target.objectId,
            listOf(
                StatusAttr.curHp(target.stats.curHp.toInt()),
                StatusAttr.maxHp(target.stats.getMaxHp().toInt())
            )
        )

        player.sendPacket(targetSelection, targetStatus)
    }
}
