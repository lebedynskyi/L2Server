package lineage.vetal.server.core.client

import lineage.vetal.server.core.encryption.BlowFishCrypt
import java.util.concurrent.atomic.AtomicBoolean

class BridgeCrypt : ClientCrypt() {
    private lateinit var blowFish: BlowFishCrypt
    private val isStaticRead = AtomicBoolean(true)
    private val isStaticWrite = AtomicBoolean(true)

    fun init(blowFishKey: ByteArray) {
        blowFish = BlowFishCrypt(blowFishKey)
    }

    override fun encrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        // The size of packet should be divided to 8
        var newSize = originalSize
        newSize += 8 - newSize % 8

        // First packet is not encrypted.
        return if (isStaticRead.getAndSet(false)) {
            originalSize
        } else {
            blowFish.encrypt(raw, offset, newSize)
        }
    }

    override fun decrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        // First packet is not encrypted
        return if (isStaticWrite.getAndSet(false)) {
            originalSize
        } else {
            blowFish.decrypt(raw, offset, originalSize )
        }
    }
}