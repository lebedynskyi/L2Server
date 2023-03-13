package lineage.vetal.server.login.game

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.model.RegisteredServer
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.login.ConfigGame
import lineage.vetal.server.login.bridgeclient.packets.client.RequestAuth
import lineage.vetal.server.login.bridgeclient.packets.client.RequestInit
import vetal.server.writeError

class GameLobby(
    private val gameConfig: ConfigGame,
    private val gameWorld: GameWorld
) {
    fun onServerConnected(client: BridgeClient) {
        val serverConfig = gameConfig.serverInfo
        val blowFishKey = serverConfig.bridgeKey
        val serverStatus = ServerStatus(
            gameConfig.serverInfo.id,
            gameWorld.currentOnline,
            true,
            gameConfig.serverInfo.ip
        )

        client.connection.crypt.init(blowFishKey.toByteArray())
        client.serverInfo = RegisteredServer(serverConfig, serverStatus)
        client.sendPacket(RequestInit(gameConfig.serverInfo.id))
    }

    fun onInitOk(client: BridgeClient) {
        val serverStatus = client.serverInfo?.status
        if (serverStatus == null) {
            client.saveAndClose()
            writeError("InitOK", "Status is null. Close connection")
            return
        }

        client.sendPacket(RequestAuth(serverStatus))
    }
}