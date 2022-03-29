package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.LoginState
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket
import lineage.vetal.server.login.clientserver.packets.server.LoginFail
import lineage.vetal.server.login.clientserver.packets.server.LoginOk
import lineage.vetal.server.login.clientserver.packets.server.ServerList
import javax.crypto.Cipher
import kotlin.random.Random

class RequestAuthLogin : LoginClientPacket() {
    private val TAG = "RequestAuthLogin"

    val raw = ByteArray(128)

    override fun execute(client: LoginClient, context: LoginContext) {
        if (client.loginState != LoginState.AUTH_GG) {
            client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
            context.loginLobby.removeClient(client)
            return
        }

        val rsaCipher = Cipher.getInstance("RSA/ECB/nopadding")
        rsaCipher.init(Cipher.DECRYPT_MODE, client.connection.getCredentialsDecryptionKey())

        val decrypted = rsaCipher.doFinal(raw, 0x00, 0x80)
        val user = String(decrypted, 0x5E, 14).trim { it <= ' ' }.lowercase()
        val password = String(decrypted, 0x6C, 16).trim { it <= ' ' }

        // TODO need DB query.. Don't store password in memory
        if (user != "qwe" || password != "qwe") {
            client.saveAndClose(LoginFail.REASON_USER_OR_PASS_WRONG)
            context.loginLobby.removeClient(client)
            return
        }

        val accountInfo = AccountInfo(user)
        if (context.loginLobby.hasAuthedClient(accountInfo)) {
            writeDebug(TAG, "Account already in lobby $accountInfo")
            client.saveAndClose()
            return
        }

        // TODO check is account in game already in game Below code should be in server packet?
        writeDebug(TAG, "New account info received $accountInfo")


        val sessionKey = SessionKey(Random.nextInt(), Random.nextInt(), Random.nextInt(), Random.nextInt())
        client.loginState = LoginState.AUTH_LOGIN
        client.sessionKey = sessionKey
        client.account = accountInfo

        if (context.config.lobbyConfig.showLicense) {
            client.sendPacket(LoginOk(sessionKey.loginOkID1, sessionKey.loginOkID2))
        } else {
            client.sendPacket(ServerList(context.loginLobby.serverList))
        }
    }

    override fun read() {
        readB(raw)
    }
}