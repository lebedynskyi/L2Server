package lineage.vetal.server.login

import kotlinx.coroutines.delay
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.server.LoginClient
import lineage.vetal.server.login.settings.LobbyConfig

class LoginLobby(
    private val lobbyConfig: LobbyConfig
) {
    private val TAG = "LoginLobby"
    private val connectedClients = mutableMapOf<Int, LoginClient>()

    suspend fun onClientConnected(client: LoginClient) {
        if (connectedClients.size >= lobbyConfig.maxCount) {
            writeInfo(TAG, "Lobby is full. reject client")
            client.saveAndClose()
            return
        }

        writeDebug(TAG, "New client added to lobby")
        delay(3000)
        connectedClients[client.sessionId] = client
        client.sendInitPacket()
    }

    fun onClientDisconnected(client: LoginClient) {
        writeDebug(TAG, "New client removed to lobby")
        connectedClients.remove(client.sessionId)
    }
}