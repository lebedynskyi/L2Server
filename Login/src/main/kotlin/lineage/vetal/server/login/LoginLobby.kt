package lineage.vetal.server.login

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.config.LobbyConfig

class LoginLobby(
    private val lobbyConfig: LobbyConfig,
) {
    private val TAG = "LoginLobby"
    private val connectedClients = mutableMapOf<Int, LoginClient>()

    fun onClientConnected(client: LoginClient) {
        if (connectedClients.size >= lobbyConfig.maxCount) {
            writeInfo(TAG, "Lobby is full. reject client")
            client.saveAndClose()
            return
        }

        writeDebug(TAG, "New $client added to lobby")
        connectedClients[client.sessionId] = client
        client.onAddedToLobby()
    }

    fun onPacketReceived(client: LoginClient, packet: ReceivablePacket) {

    }

    fun onClientDisconnected(client: LoginClient) {
        writeDebug(TAG, "$client removed from lobby")
        connectedClients.remove(client.sessionId)
        client.saveAndClose()
    }
}