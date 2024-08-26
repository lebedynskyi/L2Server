package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket
import lineage.vetal.server.game.gameserver.packet.server.ActionFailed

private const val TAG = "RequestAction"

class RequestAction : GamePacket() {
    private var actionObjectId: Int = 0
    private var originX: Int = 0
    private var originY: Int = 0
    private var originZ: Int = 0
    private var isShiftAction: Boolean = false

    override fun read() {
        actionObjectId = readD()
        originX = readD()
        originY = readD()
        originZ = readD()
        isShiftAction = readC() != 0
    }

    override fun executeImpl(client: GameClient, context: GameContext) {
        val player = client.player ?: return

        context.actionManager.onPlayerAction(player, actionObjectId, originX, originY, originZ)
        client.sendPacket(ActionFailed.STATIC_PACKET)
    }
}