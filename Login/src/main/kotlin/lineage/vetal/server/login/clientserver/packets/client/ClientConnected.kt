package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket

class ClientConnected : LoginClientPacket() {
    override fun read() {}

    override fun execute(client: LoginClient, context: LoginContext) {
        context.loginLobby.onClientConnected(client)
    }
}