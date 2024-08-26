package lineage.vetal.server.game.gameserver.packet.client


import lineage.vetal.server.game.gameserver.packet.GamePacket

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
}
