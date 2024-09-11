package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class HtmlMessage(
    private val mpcObjectId: Int,
    private val html: String
) : GameServerPacket() {
    override val opCode: Byte = 0x0f

    override fun write() {
        writeD(mpcObjectId)

        if (html.length > 8192) {
            writeS("<html><body><br>Sorry, the HTML is too long!</body></html>")
        } else {
            writeS(html)
        }
        writeD(0)
    }
}