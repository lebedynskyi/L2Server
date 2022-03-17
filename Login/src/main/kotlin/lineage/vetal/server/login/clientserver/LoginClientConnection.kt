package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.login.clientserver.packets.server.Init
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.security.PrivateKey

class LoginClientConnection(
    val loginCrypt: LoginClientCrypt,
    loginPacketParser: LoginClientPacketParser,
    socket: SocketChannel,
    selectionKey: SelectionKey,
    clientAddress: InetSocketAddress
) : ClientConnection(socket, selectionKey, clientAddress, loginCrypt, loginPacketParser) {
    private val TAG = "LoginClientConnection"

    fun sendInitPacket(sessionId: Int) {
        sendPacket(Init(sessionId, loginCrypt.scrambleModules, loginCrypt.blowFishKey))
    }

    fun getRsaPublicKey(): PrivateKey {
        return loginCrypt.rsaPair.private
    }
}