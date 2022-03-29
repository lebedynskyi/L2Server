package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket

class ClientDisconnected : LoginClientPacket() {
    override fun execute(client: LoginClient, context: LoginContext) {
        context.loginLobby.removeClient(client)
        client.loginState = null
        client.saveAndClose()
    }

    override fun read() {

    }
}