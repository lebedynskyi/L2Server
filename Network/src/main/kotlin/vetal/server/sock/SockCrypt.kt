package vetal.server.sock

abstract class SockCrypt {
    abstract fun encrypt(raw: ByteArray, offset: Int, originalSize: Int): Int
    abstract fun decrypt(raw: ByteArray, offset: Int, originalSize: Int): Int

    companion object {
        val NO_CRYPT = object : SockCrypt() {
            override fun encrypt(raw: ByteArray, offset: Int, originalSize: Int) = originalSize
            override fun decrypt(raw: ByteArray, offset: Int, originalSize: Int) = originalSize
        }
    }
}