package lineage.vetal.server.login.bridgeclient

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.bridgeclient.packets.server.AuthOk

class BridgeGamePacketHandler(
    private val context: GameContext
) {
    private val TAG = "BridgeGamePacketHandler"

    fun handle(client: BridgeGameClient, packet: ReceivablePacket) {
        if (packet !is BridgeGamePacket) {
            return
        }

        when (packet) {
            is AuthOk -> writeInfo(TAG, "Successfully connected to bridge server")
        }

        packet.execute(client, context)
    }
}