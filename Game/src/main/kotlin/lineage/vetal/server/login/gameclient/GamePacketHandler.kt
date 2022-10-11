package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.utils.ext.ifNullOrBlank
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.packet.GamePacket
import vetal.server.network.ReceivablePacket

class GamePacketHandler(
    private val context: GameContext
) {
    private val TAG = "GamePacketHandler"

    fun handle(client: GameClient, packet: ReceivablePacket) {
        if (packet !is GamePacket) {
            return
        }

        writeDebug(TAG, "Player `${client.player?.name?.ifNullOrBlank { client.account.account }}` packet -> ${packet::class.java.simpleName}")

        when (packet) {

        }

        packet.execute(client, context)
    }
}