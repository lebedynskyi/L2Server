package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket
import vetal.server.network.ReceivablePacket

class LoginClientPacketHandler(
    private val context: LoginContext
) {
    private val TAG = "LoginClientPacketHandler"

    fun handle(client: LoginClient, packet: ReceivablePacket) {
        if (packet !is LoginClientPacket) {
            return
        }

        writeDebug(TAG, packet::class.java.simpleName)

        when (packet) {

        }

        packet.execute(client, context)
    }
}