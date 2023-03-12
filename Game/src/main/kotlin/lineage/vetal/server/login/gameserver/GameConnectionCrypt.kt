package lineage.vetal.server.login.gameserver

import vetal.server.network.ConnectionCrypt
import kotlin.random.Random

class GameConnectionCrypt private constructor(
    internal val key: ByteArray
) : ConnectionCrypt() {
    companion object {
        private const val cachedKeysSize = 20
        private val CRYPT_KEYS = Array(cachedKeysSize) {
            ByteArray(16) { index ->
                when (index) {
                    in 0..7 -> Random.nextInt(255).toByte()
                    8 -> 0xc8.toByte()
                    9 -> 0x27.toByte()
                    10 -> 0x93.toByte()
                    11 -> 0x01.toByte()
                    12 -> 0xa1.toByte()
                    13 -> 0x6c.toByte()
                    14 -> 0x31.toByte()
                    15 -> 0x97.toByte()
                    else -> throw IllegalArgumentException("Unable to initialize Game crypt key")
                }
            }
        }

        fun newInstance(): GameConnectionCrypt {
            return GameConnectionCrypt(CRYPT_KEYS.random())
        }
    }

    private val inKey = ByteArray(16).also {
        System.arraycopy(key, 0, it, 0, key.size)
    }

    private val outKey = ByteArray(16).also {
        System.arraycopy(key, 0, it, 0, key.size)
    }

    private var isEnabled = false

    override fun decrypt(raw: ByteArray, offset: Int, size: Int): Int {
        if (!isEnabled) {
            return size
        }

        var temp = 0
        for (i in 0 until size) {
            val temp2: Int = raw[offset + i].toInt() and 0xFF
            raw[offset + i] = (temp2 xor inKey[i and 15].toInt() xor temp).toByte()
            temp = temp2
        }
        var old: Int = inKey[8].toInt() and 0xff
        old = old or (inKey[9].toInt() shl 8 and 0xff00)
        old = old or (inKey[10].toInt() shl 0x10 and 0xff0000)
        old = old or (inKey[11].toInt() shl 0x18 and -0x1000000)
        old += size
        inKey[8] = (old and 0xff).toByte()
        inKey[9] = (old shr 0x08 and 0xff).toByte()
        inKey[10] = (old shr 0x10 and 0xff).toByte()
        inKey[11] = (old shr 0x18 and 0xff).toByte()

        return size
    }

    override fun encrypt(raw: ByteArray, offset: Int, size: Int): Int {
        if (!isEnabled) {
            isEnabled = true
            return size
        }
        var temp = 0
        for (i in 0 until size) {
            val temp2: Int = raw[offset + i].toInt() and 0xFF
            temp = temp2 xor outKey[i and 15].toInt() xor temp
            raw[offset + i] = temp.toByte()
        }
        var old: Int = outKey[8].toInt() and 0xff
        old = old or (outKey[9].toInt() shl 8 and 0xff00)
        old = old or (outKey[10].toInt() shl 0x10 and 0xff0000)
        old = old or (outKey[11].toInt() shl 0x18 and -0x1000000)
        old += size
        outKey[8] = (old and 0xff).toByte()
        outKey[9] = (old shr 0x08 and 0xff).toByte()
        outKey[10] = (old shr 0x10 and 0xff).toByte()
        outKey[11] = (old shr 0x18 and 0xff).toByte()

        return size
    }
}