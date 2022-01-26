package lineage.vetal.server.login.server

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.core.server.*
import lineage.vetal.server.login.model.Account
import lineage.vetal.server.login.packets.server.Init

class LoginClient(
    sessionId: Int,
    clientConnection: ClientConnection,
    loginCrypt: LoginCrypt
) : Client(clientConnection, loginCrypt) {
    var loginState: LoginState = LoginState.CONNECTED
    var account: Account? = null

    init {
        sendPacket(Init(sessionId, loginCrypt.scrambleModules, loginCrypt.blowFishKey))
    }

    override fun encrypt() {

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