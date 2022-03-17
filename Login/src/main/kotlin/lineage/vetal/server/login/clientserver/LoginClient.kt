package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.login.model.AccountInfo
import lineage.vetal.server.login.model.SessionKey

class LoginClient(
    val sessionId: Int,
    override val connection: LoginClientConnection
) : Client() {
    var loginState: LoginState? = null
    var account: AccountInfo? = null
    var sessionKey: SessionKey? = null

    fun sendInitPacket() {
        connection.sendInitPacket(sessionId)
    }
}

enum class LoginState {
    CONNECTED, AUTH_GG, AUTH_LOGIN
}