package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket

class ClientDisconnected : LoginClientPacket() {
    private val TAG = "ClientConnected"

    override fun execute(client: LoginClient, context: LoginContext) {
        context.loginLobby.removeClient(client)
    }

    override fun read() {

    }
}