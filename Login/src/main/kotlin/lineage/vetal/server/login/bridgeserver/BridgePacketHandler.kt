package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.client.BridgeClient
import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.bridgeserver.packets.BridgePacket

class BridgePacketHandler(
    private val context: LoginContext
) {
    private val TAG = "BridgePacketHandler"

    fun handle(client: BridgeClient, packet: ReceivablePacket) {
        if (packet !is BridgePacket) {
            return
        }

        writeDebug(TAG, "Client -> $client packet -> ${packet::class.java.simpleName}")

        when (packet) {

        }

        packet.execute(client, context)
    }
}