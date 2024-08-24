package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket
import lineage.vetal.server.game.gameserver.packet.server.ActionFailed

private const val TAG = "RequestAction"

class RequestAction : GamePacket() {
    var actionObjectId: Int = 0
    var originX: Int = 0
    var originY: Int = 0
    var originZ: Int = 0
    var isShiftAction: Boolean = false

    override fun read() {
        actionObjectId = readD()
        originX = readD()
        originY = readD()
        originZ = readD()
        isShiftAction = readC() != 0
    }

    override fun executeImpl(client: GameClient, context: GameContext) {
        val player = client.player ?: return

        player.region.items[actionObjectId]?.let {
            context.itemManager.onPlayerPickUpItem(player, it, originX, originY, originZ)
        }

        player.region.npc[actionObjectId]?.let {
            context.actionManager.onPlayerAction(player, it, originX, originY, originZ)
        }

        player.region.players[actionObjectId]?.let {
            context.actionManager.onPlayerAction(player, it, originX, originY, originZ)
        }


        client.sendPacket(ActionFailed.STATIC_PACKET)
    }
}