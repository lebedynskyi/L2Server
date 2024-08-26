package lineage.vetal.server.game.game.manager.action

import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.server.StatusAttribute
import lineage.vetal.server.game.gameserver.packet.server.StatusUpdate
import lineage.vetal.server.game.gameserver.packet.server.TargetSelected

object SelectTargetSuccessUseCase {
    fun onSelectTargetSuccess(player: PlayerObject, actionTarget: CreatureObject) {
        player.target = actionTarget
        val playerPos = player.position
        player.sendPacket(TargetSelected(player.objectId, actionTarget.objectId, playerPos.x, playerPos.y, playerPos.z))
        player.sendPacket(
            StatusUpdate(
                actionTarget.objectId,
                listOf(
                    StatusAttribute.curHp(actionTarget.stats.curHp.toInt()),
                    StatusAttribute.maxHp(actionTarget.stats.getMaxHp().toInt())
                )
            )
        )
    }
}