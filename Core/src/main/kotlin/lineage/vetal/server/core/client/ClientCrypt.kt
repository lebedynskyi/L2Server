package lineage.vetal.server.core.client

abstract class ClientCrypt {
    abstract fun encrypt(raw: ByteArray, offset: Int, originalSize: Int): Int
    abstract fun decrypt(raw: ByteArray, offset: Int, originalSize: Int): Int
}