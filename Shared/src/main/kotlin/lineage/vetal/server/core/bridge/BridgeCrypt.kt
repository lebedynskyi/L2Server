package lineage.vetal.server.core.bridge

import lineage.vetal.server.core.encryption.BlowFishCrypt
import vetal.server.network.ClientCrypt
import java.util.concurrent.atomic.AtomicBoolean

class BridgeCrypt : ClientCrypt() {
    private lateinit var blowFish: BlowFishCrypt
    private val isStaticEncrypt = AtomicBoolean(true)
    private val isStaticDecrypt = AtomicBoolean(true)

    fun init(blowFishKey: ByteArray) {
        blowFish = BlowFishCrypt(blowFishKey)
    }

    override fun encrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        // The size of packet should be divided to 8
        var newSize = originalSize
        newSize += 8 - newSize % 8

        // First packet is not encrypted.
        return if (isStaticEncrypt.getAndSet(false)) {
            originalSize
        } else {
            blowFish.encrypt(raw, offset, newSize)
        }
    }

    override fun decrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        // First packet is not encrypted
        return if (isStaticDecrypt.getAndSet(false)) {
            originalSize
        } else {
            blowFish.decrypt(raw, offset, originalSize )
        }
    }
}