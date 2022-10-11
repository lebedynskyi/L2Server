package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.LoginState
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket
import lineage.vetal.server.login.clientserver.packets.server.LoginFail

class ClientConnected : LoginClientPacket() {
    private val TAG = "ClientConnected"

    override fun execute(client: LoginClient, context: LoginContext) {
        if (context.loginLobby.acceptNewClient(client)) {
            writeDebug(TAG, "New $client added to lobby")
            client.loginState = LoginState.CONNECTED
            client.sendInitPacket()
            return
        }

        writeInfo(TAG, "Lobby is full. reject client")
        client.saveAndClose(LoginFail.REASON_ACCESS_FAILED)
    }

    override fun read() {

    }
}