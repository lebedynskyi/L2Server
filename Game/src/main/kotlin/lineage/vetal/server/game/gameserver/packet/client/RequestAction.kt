package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.core.utils.logs.writeError
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
        val targetItem = player.region.items[actionObjectId]
        if (targetItem != null) {
            context.itemManager.onPlayerPickUpItem(player, targetItem, originX, originY, originZ)
        }

        val targetNpc = player.region.npc[actionObjectId]
        if (targetNpc != null) {
            writeError(TAG, "Request target of NPC is not implemented")
        }

        if (player.objectId == actionObjectId) {
            writeError(TAG, "Request SELF target is not implemented")
        }

        client.sendPacket(ActionFailed.STATIC_PACKET)
    }
}