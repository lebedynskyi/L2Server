package lineage.vetal.server.core.client

import lineage.vetal.server.core.encryption.BlowFishCrypt

class BridgeCrypt : ClientCrypt() {
    fun init(blowFishKey: ByteArray) {
        blowFish = BlowFishCrypt(blowFishKey)
    }

    private var blowFish: BlowFishCrypt? = null

    override fun encrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        return blowFish?.encrypt(raw, offset, originalSize) ?: originalSize
    }

    override fun decrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        return blowFish?.decrypt(raw, offset, originalSize) ?: originalSize
    }
}