package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.login.model.AccountInfo

class LoginClient(
    val sessionId: Int,
    override val connection: LoginClientConnection
) : Client() {
    private val TAG = "LoginClient"

    var loginState: LoginState? = null
    var account: AccountInfo? = null

    fun onAddedToLobby() {
        loginState = LoginState.CONNECTED
        connection.sendInitPacket(sessionId)
    }

    override fun toString(): String {
        return "Client id $sessionId ip ${connection.clientAddress.address.hostAddress}"
    }
}

enum class LoginState {
    CONNECTED
}