package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey
import vetalll.server.sock.SockClient

class LoginClient(
    val sessionId: Int,
    override val connection: LoginClientConnection
) : SockClient(connection) {
    var loginState: LoginState? = null
    var account: AccountInfo? = null
    var sessionKey: SessionKey? = null

    fun sendInitPacket() {
        connection.sendInitPacket(sessionId)
    }

    override fun toString(): String {
        return if (account != null) {
            "LoginClient: ${account?.account} address $connection"
        } else {
            "LoginClient: address $connection"
        }
    }
}

enum class LoginState {
    CONNECTED, AUTH_GG, AUTH_LOGIN
}