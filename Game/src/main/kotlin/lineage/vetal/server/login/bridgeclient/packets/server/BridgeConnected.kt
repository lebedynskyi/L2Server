package lineage.vetal.server.login.bridgeclient.packets.server

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.model.RegisteredServer
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.bridgeclient.packets.client.RequestInit
import lineage.vetal.server.login.game.GameContext

class BridgeConnected : BridgeGamePacket() {
    override fun execute(client: BridgeClient, context: GameContext) {
        val serverConfig = context.config.serverInfo
        val blowFishKey = serverConfig.bridgeKey
        val serverStatus = ServerStatus(
            context.config.serverInfo.id,
            context.gameWorld.currentOnline,
            true,
            context.config.serverInfo.ip
        )

        client.connection.crypt.init(blowFishKey.toByteArray())
        client.serverInfo = RegisteredServer(serverConfig, serverStatus)
        client.sendPacket(RequestInit(context.config.serverInfo.id))
    }

    override fun read() {

    }
}