package lineage.vetal.server.login

import lineage.vetal.server.core.ConfigRegisteredServer
import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.login.clientserver.LoginClient

class LoginLobby(
    val configLoginLobby: ConfigLoginLobby,
    val registeredServers: List<ConfigRegisteredServer>
) {
    private val TAG = "LoginLobby"
    private val connectedClients = mutableMapOf<Int, LoginClient>()

    fun acceptNewClient(client: LoginClient): Boolean {
        return if (canAcceptMoreClients()) {
            connectedClients[client.sessionId] = client
            true
        } else {
            false
        }
    }

    fun removeClient(client: LoginClient) {
        connectedClients.remove(client.sessionId)
    }

    fun hasAuthedClient(accountInfo: AccountInfo): Boolean {
        return connectedClients.values.firstOrNull { it.account?.account == accountInfo.account } != null
    }

    fun canAcceptMoreClients(): Boolean {
        return connectedClients.size < configLoginLobby.maxCount
    }

    fun getRegisteredServer(serverId: Int): ConfigRegisteredServer? {
        return registeredServers.firstOrNull { it.id == serverId }
    }

    fun updateServerStatus(serverStatus: ServerStatus): Boolean {
        registeredServers.firstOrNull { it.id == serverStatus.id }?.let {
            it.serverStatus = serverStatus
            return true
        }

        return false
    }
}