package com.vetalll.core.encryption

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.RSAKeyGenParameterSpec

class CryptUtil {
    companion object {
        private val keygen = KeyPairGenerator.getInstance("RSA").apply {
            initialize(RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4))
        }

        private val secureRandom = SecureRandom()


        fun generateRsa128PublicKeyPair(): KeyPair {
            return keygen.genKeyPair()
        }

        fun generateByteArray(size: Int): ByteArray {
            return secureRandom.generateSeed(size)
        }

        /**
         * Packet is first XOR encoded with <code>key</code>.<br>
         * Then, the last 4 bytes are overwritten with the the XOR "key".<br>
         * Thus this assume that there is enough room for the key to fit without overwriting data.
         * @param raw The raw bytes to be encrypted
         * @param offset The begining of the data to be encrypted
         * @param size Length of the data to be encrypted
         * @param key The 4 bytes (int) XOR key
         */
        fun encodeXor(raw: ByteArray, offset: Int, size: Int, key: Int) {
            val stop = size - 8
            var pos = 4 + offset
            var edx: Int
            var ecx = key // Initial xor key

            while (pos < stop) {
                edx = raw[pos].toInt() and 0xFF
                edx = edx or (raw[pos + 1].toInt() and 0xFF shl 8)
                edx = edx or (raw[pos + 2].toInt() and 0xFF shl 16)
                edx = edx or (raw[pos + 3].toInt() and 0xFF shl 24)

                ecx += edx
                edx = edx xor ecx

                raw[pos++] = (edx and 0xFF).toByte()
                raw[pos++] = (edx shr 8 and 0xFF).toByte()
                raw[pos++] = (edx shr 16 and 0xFF).toByte()
                raw[pos++] = (edx shr 24 and 0xFF).toByte()
            }

            raw[pos++] = (ecx and 0xFF).toByte()
            raw[pos++] = (ecx shr 8 and 0xFF).toByte()
            raw[pos++] = (ecx shr 16 and 0xFF).toByte()
            raw[pos] = (ecx shr 24 and 0xFF).toByte()
        }

        fun verifyChecksum(raw: ByteArray, offset: Int, size: Int): Boolean {
            // check if size is multiple of 4 and if there is more then only the checksum
            if (size and 3 != 0 || size <= 4) return false
            var chksum: Long = 0
            val count = size - 4
            var check: Long = -1
            var i: Int = offset
            while (i < count) {
                check = (raw[i].toLong() and 0xff)
                check = check or (raw[i + 1].toLong() shl 8 and 0xff00)
                check = check or (raw[i + 2].toLong() shl 0x10 and 0xff0000)
                check = check or (raw[i + 3].toLong() shl 0x18 and -0x1000000)
                chksum = chksum xor check
                i += 4
            }
            check = (raw[i].toInt() and 0xff).toLong()
            check = check or (raw[i + 1].toLong() shl 8 and 0xff00)
            check = check or (raw[i + 2].toLong() shl 0x10 and 0xff0000)
            check = check or (raw[i + 3].toLong() shl 0x18 and -0x1000000)
            return check == chksum
        }

        fun appendChecksum(raw: ByteArray, offset: Int, size: Int) {
            var chksum: Long = 0
            val count = size - 4
            var ecx: Long
            var i = offset
            while (i < count) {
                ecx = (raw[i].toLong() and 0xff)
                ecx = ecx or (raw[i + 1].toLong() shl 8 and 0xff00)
                ecx = ecx or (raw[i + 2].toLong() shl 0x10 and 0xff0000)
                ecx = ecx or (raw[i + 3].toLong() shl 0x18 and -0x1000000)
                chksum = chksum xor ecx
                i += 4
            }
            ecx = (raw[i].toLong() and 0xff)
            ecx = ecx or (raw[i + 1].toLong() shl 8 and 0xff00)
            ecx = ecx or (raw[i + 2].toLong() shl 0x10 and 0xff0000)
            ecx = ecx or (raw[i + 3].toLong() shl 0x18 and -0x1000000)
            raw[i] = (chksum and 0xff).toByte()
            raw[i + 1] = (chksum shr 0x08 and 0xff).toByte()
            raw[i + 2] = (chksum shr 0x10 and 0xff).toByte()
            raw[i + 3] = (chksum shr 0x18 and 0xff).toByte()
        }
    }
}