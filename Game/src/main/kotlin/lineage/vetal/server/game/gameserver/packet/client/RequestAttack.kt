package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.core.utils.ext.toBoolean
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket
import lineage.vetal.server.game.gameserver.packet.server.ActionFailed

class RequestAttack : GamePacket() {
    private var objectId: Int = 0
    private var originX: Int = 0
    private var originY: Int = 0
    private var originZ: Int = 0
    private var attackId: Boolean = false // false for simple click  true for shift-click

    override fun read() {
        objectId = readD();
        originX = readD();
        originY = readD();
        originZ = readD();
        attackId = readC().toBoolean();
    }

    override fun executeImpl(client: GameClient, context: GameContext) {
        val player = client.player ?: return
        // TODO. need extract logic for attack. for action manager
        context.actionManager.onPlayerAction(player, objectId)
        player.sendPacket(ActionFailed.STATIC_PACKET)
    }
}