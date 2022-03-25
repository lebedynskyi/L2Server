package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.packet.GamePacket


class ProtocolVersion : GamePacket() {
    var version: Int = -1

    override fun execute(client: GameClient, context: GameContext) {
        when (version) {
            737 -> client.sendInitPacket()
            740 -> client.sendInitPacket()
            744 -> client.sendInitPacket()
            746 -> client.sendInitPacket()
            else -> client.saveAndClose()
        }
    }

    override fun read() {
        version = readD()
    }
}