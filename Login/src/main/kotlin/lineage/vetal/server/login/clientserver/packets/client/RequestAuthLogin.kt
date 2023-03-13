package lineage.vetal.server.login.clientserver.packets.client

import lineage.vetal.server.login.LoginContext
import lineage.vetal.server.login.clientserver.LoginClient
import lineage.vetal.server.login.clientserver.packets.LoginClientPacket
import javax.crypto.Cipher

class RequestAuthLogin : LoginClientPacket() {
    private val packetData = ByteArray(128)

    override fun read() {
        readB(packetData)
    }

    override fun execute(client: LoginClient, context: LoginContext) {
        val rsaCipher = Cipher.getInstance("RSA/ECB/nopadding")
        rsaCipher.init(Cipher.DECRYPT_MODE, client.connection.getCredentialsDecryptionKey())
        val decrypted = rsaCipher.doFinal(packetData, 0x00, 0x80)
        val login = String(decrypted, 0x5E, 14).trim { it <= ' ' }.lowercase()
        val password = String(decrypted, 0x6C, 16).trim { it <= ' ' }
        context.loginLobby.requestLoginAuth(client, login, password)
    }
}