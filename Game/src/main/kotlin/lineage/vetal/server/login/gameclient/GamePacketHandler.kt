package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.packet.GamePacket

class GamePacketHandler(
    private val context: GameContext
) {
    private val TAG = "GamePacketHandler"

    fun handle(client: GameClient, packet: ReceivablePacket) {
        if (packet !is GamePacket) {
            return
        }

        writeDebug(TAG, "Client -> $client packet -> ${packet::class.java.simpleName}")

        when (packet) {

        }

        packet.execute(client, context)
    }
}