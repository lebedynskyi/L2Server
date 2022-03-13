package lineage.vetal.server.login.clientserver.packets

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.login.clientserver.LoginClient

abstract class PacketHandler {
    abstract fun handlePacket(client: LoginClient, packet: ReceivablePacket)
}