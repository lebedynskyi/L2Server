package lineage.vetal.server.login

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.model.ServerInfo

class LoginLobby(
    private val lobbyConfig: ConfigLoginLobby,
    val registeredServers: List<ServerInfo>
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
        return connectedClients.size < lobbyConfig.maxCount
    }

    fun getRegisteredServer(serverId: Int): ServerInfo? {
        return registeredServers.firstOrNull { it.config.id == serverId }
    }

    fun updateServerStatus(serverStatus: ServerStatus): Boolean {
        registeredServers.firstOrNull { it.config.id == serverStatus.id }?.let {
            it.status = serverStatus
            return true
        }

        return false
    }
}