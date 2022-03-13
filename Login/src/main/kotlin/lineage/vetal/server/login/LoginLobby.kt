package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.server.LoginClient
import lineage.vetal.server.login.settings.LobbyConfig

class LoginLobby(
    private val lobbyConfig: LobbyConfig,
) {
    private val TAG = "LoginLobby"
    private val connectedClients = mutableMapOf<Int, LoginClient>()
    private val lobbyPacketHandler = LobbyPacketHandler(this)

    fun onClientConnected(client: LoginClient) {
        if (connectedClients.size >= lobbyConfig.maxCount) {
            writeInfo(TAG, "Lobby is full. reject client")
            client.saveAndClose()
            return
        }

        writeDebug(TAG, "New client added to lobby")
        connectedClients[client.sessionId] = client
        client.sendInitPacket()
    }

    fun onPacketReceived() {
        lobbyPacketHandler.handlePacket()
    }

    fun onClientDisconnected(client: LoginClient) {
        writeDebug(TAG, "Client removed from lobby")
        connectedClients.remove(client.sessionId)
        client.saveAndClose()
    }
}