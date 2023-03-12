package vetal.server.network

abstract class ConnectionCrypt {
    abstract fun encrypt(raw: ByteArray, offset: Int, originalSize: Int): Int
    abstract fun decrypt(raw: ByteArray, offset: Int, originalSize: Int): Int
}