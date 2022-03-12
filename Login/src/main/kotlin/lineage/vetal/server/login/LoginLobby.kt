package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.server.LoginClient
import lineage.vetal.server.login.settings.LobbyConfig

class LoginLobby(
    private val lobbyConfig: LobbyConfig
) {
    private val connectedClients = mutableMapOf<Int, LoginClient>()

    fun onClientConnected(client: LoginClient) {
        if (connectedClients.size >= lobbyConfig.maxCount) {
            client.saveAndClose()
            return
        }

        // TODO check state. Multiple login ? Etc

        connectedClients[client.sessionId] = client
        writeDebug(TAG, "New client added to lobby")

        client.sendInitPacket()
    }

    fun onClientDisconnected(client: LoginClient) {
        connectedClients.remove(client.sessionId)
    }

    companion object {
        const val TAG = "LoginLobby"
    }
}