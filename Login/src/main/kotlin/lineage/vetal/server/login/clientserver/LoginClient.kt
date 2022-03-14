package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.login.model.AccountInfo
import lineage.vetal.server.login.model.SessionKey

class LoginClient(
    val sessionId: Int,
    override val connection: LoginClientConnection
) : Client() {
    private val TAG = "LoginClient"

    var loginState: LoginState? = null
    var account: AccountInfo? = null
    var sessionKey: SessionKey? = null

    override fun toString(): String {
        return "Client id $sessionId ip ${connection.clientAddress.address.hostAddress}"
    }

    fun sendInitPacket() {
        connection.sendInitPacket(sessionId)
    }
}

enum class LoginState {
    CONNECTED, AUTH_GG, AUTH_LOGIN
}