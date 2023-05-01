package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket


class RequestProtocolVersion : GamePacket() {
    var version: Int = -1

    override fun read() {
        version = readD()
    }

    override fun execute(client: GameClient, context: GameContext) {
        when (version) {
            737 -> client.sendInitPacket()
            740 -> client.sendInitPacket()
            744 -> client.sendInitPacket()
            746 -> client.sendInitPacket()
            else -> {
                writeInfo("PROTOCOL", "Unknown protocol version $version")
                client.saveAndClose()
            }
        }
    }
}