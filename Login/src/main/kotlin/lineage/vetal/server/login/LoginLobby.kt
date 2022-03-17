package lineage.vetal.server.login

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.client.BridgeClient
import lineage.vetal.server.login.bridgeserver.packets.client.RequestAuth
import lineage.vetal.server.login.bridgeserver.packets.client.RequestInit
import lineage.vetal.server.login.bridgeserver.packets.client.RequestUpdate
import lineage.vetal.server.login.bridgeserver.packets.server.AuthOk
import lineage.vetal.server.login.bridgeserver.packets.server.InitOK
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.LoginState
import lineage.vetal.server.login.clientserver.packets.client.RequestAuthLogin
import lineage.vetal.server.login.clientserver.packets.client.RequestGGAuth
import lineage.vetal.server.login.clientserver.packets.client.RequestServerList
import lineage.vetal.server.login.clientserver.packets.client.RequestServerLogin
import lineage.vetal.server.login.clientserver.packets.server.*
import lineage.vetal.server.login.model.AccountInfo
import lineage.vetal.server.login.model.LobbyConfig
import lineage.vetal.server.core.model.ServerInfo
import lineage.vetal.server.login.model.SessionKey
import javax.crypto.Cipher
import kotlin.random.Random

class LoginLobby(
    private val lobbyConfig: LobbyConfig,
    private val registeredServers: Array<ServerInfo>
) {
    private val TAG = "LoginLobby"
    private val connectedClients = mutableMapOf<Int, LoginClient>()

    fun onClientConnected(client: LoginClient) {
        if (connectedClients.size >= lobbyConfig.maxCount) {
            writeInfo(TAG, "Lobby is full. reject client")
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
            return
        }

        writeDebug(TAG, "New $client added to lobby")
        connectedClients[client.sessionId] = client

        client.loginState = LoginState.CONNECTED
        client.sendInitPacket()
    }

    fun onClientPacketReceived(client: LoginClient, packet: ReceivablePacket) {
        try {
            handleClientPacket(client, packet)
        } catch (e: Throwable) {
            writeError(TAG, "Cannot handle packet", e)
            onClientDisconnected(client, LoginFail.REASON_USER_OR_PASS_WRONG)
        }
    }

    fun onBridgePacketReceived(client: BridgeClient, packet: ReceivablePacket) {
        handleBridgePacket(client, packet)
    }

    fun onBridgeClientDisconnected(client: BridgeClient) {

    }

    fun onClientDisconnected(client: LoginClient, reason: LoginFail? = null) {
        client.loginState = null
        connectedClients.remove(client.sessionId)?.let {
            writeDebug(TAG, "$client removed from lobby")
        }
        client.saveAndClose(reason)
    }

    private fun handleBridgePacket(client: BridgeClient, packet: ReceivablePacket) {
        when (packet) {
            is RequestInit -> {
                val server = registeredServers.firstOrNull { it.id == packet.serverId }
                if (server == null) {
                    client.saveAndClose()
                } else {
                    writeInfo(TAG, "Server with id ${server.id} connected")
                    val clientConnection = client.connection
                    clientConnection.crypt.init(server.blowFishKey.toByteArray())
                    client.sendPacket(InitOK())
                }
            }

            is RequestAuth -> {
                registeredServers.firstOrNull { it.id == packet.serverInfo.id }?.let {
                    it.isOnline = true
                    writeDebug(TAG, "Server info received $packet")
                    client.sendPacket(AuthOk())
                }
            }

            is RequestUpdate -> {
                registeredServers.firstOrNull { it.id == packet.serverInfo.id }?.let {
                    it.isOnline = true
                    writeDebug(TAG, "Server info updated $packet")
                    client.sendPacket(AuthOk())
                }
            }
        }
    }

    private fun handleClientPacket(client: LoginClient, packet: ReceivablePacket) {
        when (packet) {
            is RequestGGAuth -> {
                if (client.loginState == LoginState.CONNECTED && packet.sessionId == client.sessionId) {
                    client.loginState = LoginState.AUTH_GG
                    client.sendPacket(GGAuth(client.sessionId))
                } else {
                    onClientDisconnected(client, LoginFail.REASON_ACCESS_FAILED)
                }
            }
            is RequestAuthLogin -> {
                if (client.loginState != LoginState.AUTH_GG) {
                    onClientDisconnected(client, LoginFail.REASON_ACCESS_FAILED)
                    return
                }

                val rsaCipher = Cipher.getInstance("RSA/ECB/nopadding")
                rsaCipher.init(Cipher.DECRYPT_MODE, client.connection.getRsaPublicKey())
                val decrypted = rsaCipher.doFinal(packet.raw, 0x00, 0x80)

                val user = String(decrypted, 0x5E, 14).trim { it <= ' ' }.lowercase()
                val password = String(decrypted, 0x6C, 16).trim { it <= ' ' }

                if (user != "qwe" || password != "qwe") {
                    onClientDisconnected(client, LoginFail.REASON_USER_OR_PASS_WRONG)
                    return
                }

                val sessionKey = SessionKey(Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt())
                val accountInfo = AccountInfo(user, password)

                writeDebug(TAG, "New account info received $accountInfo")

                client.loginState = LoginState.AUTH_LOGIN
                client.sessionKey = sessionKey
                client.account = accountInfo
                if (lobbyConfig.showLicense) {
                    client.sendPacket(LoginOk(sessionKey.loginOkID1, sessionKey.loginOkID2))
                } else {
                    client.sendPacket(ServerList(registeredServers.toList()))
                }
            }
            is RequestServerList -> {
                if (client.sessionKey?.loginOkID1 != packet.sessionKey1 || client.sessionKey?.loginOkID2 != packet.sessionKey2) {
                    onClientDisconnected(client, LoginFail.REASON_ACCESS_FAILED)
                    return
                }

                client.sendPacket(ServerList(registeredServers.toList()))
            }
            is RequestServerLogin -> {
                val key = client.sessionKey
                if (key != null) {
                    client.sendPacket(PlayOk(key.playOkID1, key.playOkID2))
                } else {
                    onClientDisconnected(client, LoginFail.REASON_ACCESS_FAILED)
                }
            }
        }
    }
}