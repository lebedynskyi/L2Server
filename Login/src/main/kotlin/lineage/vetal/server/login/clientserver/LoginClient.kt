package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey
import vetal.server.network.Client

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

    override fun toString(): String {
        return if (account != null) {
            "LoginClient: address $connection"
        } else {
            "LoginClient: ${account?.account} address $connection"
        }
    }
}

enum class LoginState {
    CONNECTED, AUTH_GG, AUTH_LOGIN
}