package lineage.vetal.server.login.clientserver

import lineage.vetal.server.login.clientserver.packets.server.Init
import vetal.server.sock.SockConnection
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.security.PrivateKey

// TODO wrap some how credentials keys
class LoginClientConnection(
    private val loginCrypt: LoginConnectionCrypt,
    loginPacketParser: LoginClientPacketParser,
    socket: SocketChannel,
    selector: Selector,
    selectionKey: SelectionKey,
) : SockConnection(socket, selector, selectionKey, loginPacketParser, loginCrypt) {
    private val TAG = "LoginClientConnection"

    fun sendInitPacket(sessionId: Int) {
        // modules - credentials encryption key
        sendPacket(Init(sessionId, loginCrypt.scrambleModules, loginCrypt.blowFishKey))
    }

    fun getCredentialsDecryptionKey(): PrivateKey {
        // Private = credentials decryption key
        return loginCrypt.rsaPair.private
    }
}