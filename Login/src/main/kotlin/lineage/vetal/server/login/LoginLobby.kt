package lineage.vetal.server.login

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.core.model.RegisteredServer
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.clientserver.LoginState
import lineage.vetal.server.login.clientserver.packets.server.*
import lineage.vetal.server.login.db.LoginDatabase
import java.util.*
import kotlin.random.Random

private const val TAG = "LoginLobby"

class LoginLobby(
    private val lobbyConfig: ConfigLoginLobby,
    private val loginDatabase: LoginDatabase,
    private val registeredServers: List<RegisteredServer>
) {
    private val connectedClients = mutableMapOf<Int, LoginClient>()

    fun onClientConnected(client: LoginClient) {
        if (canAddMoreClient() && addConnectedClient(client)) {
            writeDebug(TAG, "New $client added to lobby")
            client.loginState = LoginState.CONNECTED
            client.sendInitPacket()
        } else {
            writeInfo(TAG, "Lobby is full. reject client")
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
        }
    }

    fun onClientDisconnected(client: LoginClient) {
        // TODO check if client in gameServer pass it. else remove and cleanup
    }

    fun requestGGAuth(client: LoginClient, sessionId: Int) {
        if (client.loginState == LoginState.CONNECTED && sessionId == client.sessionId) {
            client.loginState = LoginState.AUTH_GG
            client.sendPacket(GGAuth(client.sessionId))
        } else {
            removeConnectedClient(client)
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
        }
    }

    fun requestLoginAuth(client: LoginClient, login: String, password: String) {
        if (client.loginState != LoginState.AUTH_GG) {
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
            removeConnectedClient(client)
            return
        }

        val accountsDao = loginDatabase.accountsDao
        var accountInfo = accountsDao.findAccount(login)
        if (accountInfo == null && lobbyConfig.autoRegistration)  {
            accountInfo = AccountInfo(UUID.randomUUID(), login, password)
            accountsDao.insertAccount(accountInfo)
        }

        if (accountInfo?.account != login || accountInfo.password != password) {
            client.saveAndClose(LoginFail.REASON_USER_OR_PASS_WRONG)
            removeConnectedClient(client)
            return
        }

        if (hasConnectedClient(accountInfo)) {
            writeDebug(TAG, "Account already in use $accountInfo")
            client.saveAndClose(LoginFail.REASON_ACCOUNT_IN_USE)
            client.saveAndClose()
            return
        }

        writeDebug(TAG, "New user connected. Login -> $accountInfo")
        val sessionKey = SessionKey(Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt())
        client.loginState = LoginState.AUTH_LOGIN
        client.sessionKey = sessionKey
        client.account = accountInfo

        if (lobbyConfig.showLicense) {
            client.sendPacket(LoginOk(sessionKey.loginOkID1, sessionKey.loginOkID2))
        } else {
            client.sendPacket(ServerList(registeredServers))
        }
    }

    fun requestServerList(client: LoginClient, sessionKey1: Int, sessionKey2: Int) {
        if (client.sessionKey?.loginOkID1 != sessionKey1 || client.sessionKey?.loginOkID2 != sessionKey2) {
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
            removeConnectedClient(client)
            return
        }

        client.sendPacket(ServerList(registeredServers))
    }

    fun requestServerLogin(client: LoginClient, serverId: Int, sessionKey1: Int, sessionKey2: Int) {
        val key = client.sessionKey
        if (key != null) {
            client.sendPacket(PlayOk(key.playOkID1, key.playOkID2))
        } else {
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
        }
    }

    private fun canAddMoreClient(): Boolean {
        return connectedClients.size <= lobbyConfig.maxCount
    }

    private fun hasConnectedClient(accountInfo: AccountInfo): Boolean {
        return connectedClients.values.firstOrNull { it.account?.account == accountInfo.account } != null
    }

    private fun addConnectedClient(client: LoginClient): Boolean {
        if (canAddMoreClient()) {
            connectedClients[client.sessionId] = client
            return true
        }

        return false
    }

    private fun removeConnectedClient(client: LoginClient) {
        connectedClients.remove(client.sessionId)
    }

    /// OLD !!!!!OLD !!!!!OLD !!!!!OLD !!!!!OLD !!!!!OLD !!!!!OLD !!!!!OLD !!!!!OLD !!!!!OLD !!!!!
    // TODO introduce BridgeLobby ?
    fun getRegisteredServer(serverId: Int): RegisteredServer? {
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