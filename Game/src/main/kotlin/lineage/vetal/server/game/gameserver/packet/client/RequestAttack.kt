package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.core.utils.ext.toBoolean
import lineage.vetal.server.game.gameserver.packet.GameClientPacket

class RequestAttack : GameClientPacket() {
    var objectId: Int = 0
    var originX: Int = 0
    var originY: Int = 0
    var originZ: Int = 0
    var attackId: Boolean = false // false for simple click  true for shift-click

    override fun read() {
        objectId = readD();
        originX = readD();
        originY = readD();
        originZ = readD();
        attackId = readC().toBoolean();
    }
}