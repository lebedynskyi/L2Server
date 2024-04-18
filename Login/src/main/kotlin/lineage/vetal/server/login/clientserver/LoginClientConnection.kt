package lineage.vetal.server.login.clientserver

import lineage.vetal.server.login.clientserver.packets.server.Init
import vetalll.server.sock.SockConnection
import java.security.PrivateKey

// TODO wrap some how credentials keys
class LoginClientConnection(
    loginPacketParser: LoginClientPacketParser,
    private val loginCrypt: LoginConnectionCrypt
) : SockConnection(loginPacketParser, loginCrypt) {
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