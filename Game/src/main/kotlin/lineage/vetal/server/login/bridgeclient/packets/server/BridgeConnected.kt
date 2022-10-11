package lineage.vetal.server.login.bridgeclient.packets.server

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.bridgeclient.packets.client.RequestInit

class BridgeConnected : BridgeGamePacket() {
    override fun execute(client: BridgeClient, context: GameContext) {
        val blowFishKey = context.config.serverInfo.bridgeKey
        client.connection.crypt.init(blowFishKey.toByteArray())
        client.serverStatus = ServerStatus(
            context.config.serverInfo.id,
            context.gameWorld.currentOnline,
            true,
            context.config.serverInfo.ip
        )

        client.sendPacket(RequestInit(context.config.serverInfo.id))
    }

    override fun read() {

    }
}