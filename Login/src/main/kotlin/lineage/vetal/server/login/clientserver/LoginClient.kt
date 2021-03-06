package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.server.Client
import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey

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