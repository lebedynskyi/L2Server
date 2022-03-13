package lineage.vetal.server.login.clientserver

import lineage.vetal.server.core.encryption.BlowFishCrypt
import lineage.vetal.server.core.encryption.CryptUtil
import lineage.vetal.server.core.client.ClientCrypt
import java.math.BigInteger
import java.security.KeyPair
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

class LoginClientCrypt(
    val blowFishKey: ByteArray,
    val rsaPair: KeyPair
) : ClientCrypt() {

    private val staticCrypt = BlowFishCrypt(STATIC_BLOW_FISH_KEY)
    private val generalCrypt = BlowFishCrypt(blowFishKey)
    val scrambleModules = scrambleModulus((rsaPair.public as RSAPublicKey).modulus)

    private var isStatic = AtomicBoolean(true)

    // This is side effect function. Original array inside buffer will be modified
    override fun encrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        // Reserve for checksum
        var newSize = originalSize + 4
        return if (isStatic.getAndSet(false)) {
            // Reserve for XOR key in the end
            newSize += 4

            // Padding.. The size of packet should be divided by 8
            newSize += 8 - newSize % 8

            CryptUtil.encodeXor(raw, offset, newSize, Random.nextInt())
            staticCrypt.encrypt(raw, offset, newSize)
        } else {
            // Padding.. The size of packet should be divided by 8
            newSize += 8 - newSize % 8
            CryptUtil.appendChecksum(raw, offset, newSize)
            generalCrypt.encrypt(raw, offset, newSize)
        }
    }

    override fun decrypt(raw: ByteArray, offset: Int, originalSize: Int): Int {
        return generalCrypt.decrypt(raw, offset, originalSize)
    }

    private fun scrambleModulus(modulus: BigInteger): ByteArray {
        var scrambledMod = modulus.toByteArray()
        if (scrambledMod.size == 0x81 && scrambledMod[0].toInt() == 0x00) {
            val temp = ByteArray(0x80)
            System.arraycopy(scrambledMod, 1, temp, 0, 0x80)
            scrambledMod = temp
        }
        // step 1 : 0x4d-0x50 <-> 0x00-0x04
        for (i in 0..3) {
            val temp = scrambledMod[i]
            scrambledMod[i] = scrambledMod[0x4d + i]
            scrambledMod[0x4d + i] = temp
        }
        // step 2 : xor first 0x40 bytes with last 0x40 bytes
        for (i in 0..0x3f) scrambledMod[i] = (scrambledMod[i].toInt() xor scrambledMod[0x40 + i].toInt()).toByte()
        // step 3 : xor bytes 0x0d-0x10 with bytes 0x34-0x38
        for (i in 0..3) scrambledMod[0x0d + i] =
            (scrambledMod[0x0d + i].toInt() xor scrambledMod[0x34 + i].toInt()).toByte()
        // step 4 : xor last 0x40 bytes with first 0x40 bytes
        for (i in 0..0x3f) scrambledMod[0x40 + i] =
            (scrambledMod[0x40 + i].toInt() xor scrambledMod[i].toInt()).toByte()
        return scrambledMod
    }

    companion object {
        private val STATIC_BLOW_FISH_KEY = ByteArray(16) {
            when (it) {
                0 -> 0x6B.toByte()
                1 -> 0x60.toByte()
                2 -> 0xCB.toByte()
                3 -> 0x5B.toByte()
                4 -> 0x82.toByte()
                5 -> 0xCE.toByte()
                6 -> 0x90.toByte()
                7 -> 0xB1.toByte()
                8 -> 0xCC.toByte()
                9 -> 0x2B.toByte()
                10 -> 0x6C.toByte()
                11 -> 0x55.toByte()
                12 -> 0x6C.toByte()
                13 -> 0x6C.toByte()
                14 -> 0x6C.toByte()
                15 -> 0x6C.toByte()
                else -> throw IllegalArgumentException("Static key should not have more than 16 elements")
            }
        }
    }
}