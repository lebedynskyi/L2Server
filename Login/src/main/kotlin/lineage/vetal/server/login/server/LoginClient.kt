package lineage.vetal.server.login.server

import lineage.vetal.server.core.server.*
import lineage.vetal.server.login.model.Account

class LoginClient(
    clientConnection: ClientConnection
) : Client(clientConnection) {
    var loginState: LoginState = LoginState.CONNECTED
    var account: Account? = null

    init {

    }

    override fun readPacket(packet: ReceivablePacket) {

    }

    override fun sendPacket(packet: SendablePacket) {
        clientConnection.sendPacket(packet)
    }
}

enum class LoginState {
    CONNECTED
}