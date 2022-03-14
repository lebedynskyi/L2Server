package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.client.ClientCrypt

class BridgeCrypt : ClientCrypt() {
    override fun encrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        TODO("Not yet implemented")
    }

    override fun decrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        TODO("Not yet implemented")
    }
}