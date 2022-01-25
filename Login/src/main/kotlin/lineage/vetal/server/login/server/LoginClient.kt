package lineage.vetal.server.login.server

import lineage.vetal.server.core.server.ClientConnection
import lineage.vetal.server.login.model.Account

class LoginClient(
    private val clientConnection: ClientConnection
) {
    var loginState: LoginState = LoginState.CONNECTED
    var account: Account? = null
}

enum class LoginState {
    CONNECTED
}