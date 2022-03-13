package lineage.vetal.server.core.encryption

class BlowFishCrypt(
    private val key: ByteArray
) {
    private val encryption = BlowfishEngine().apply { init(true, key) }
    private val decryption = BlowfishEngine().apply { init(false, key) }

    fun encrypt(raw: ByteArray, offset: Int, size: Int): Int {
        val blockCount = size / 8
        val result = ByteArray(size)

        for (i in 0 until blockCount) {
            encryption.processBlock(raw, offset + i * 8, result, i * 8)
        }

        System.arraycopy(result, 0, raw, offset, size)
        return result.size
    }

    fun decrypt(raw: ByteArray, offset: Int, size: Int): Int {
        val blockCount = size / 8
        val result = ByteArray(size)

        for (i in 0 until blockCount) {
            decryption.processBlock(raw, offset + i * 8, result, i * 8)
        }

        System.arraycopy(result, 0, raw, offset, size)
        return result.size
    }
}