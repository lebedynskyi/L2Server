package vetal.server.sock

import java.nio.ByteBuffer

abstract class SockPacket {
    override fun toString(): String {
        return this::class.java.simpleName
    }
}

/**
char   – can be in range -128 до 127. Size - 1 byte
short  – can be in range -32768 до 32767. Size - 2 byte
int    – can be in range -2147483648 до 2147483647. Size - 4 byte
int64  – can be in range -9223372036854775808 до 9223372036854775807. Size - 8 byte
float  – can be in range 2.22507e-308 до 1.79769e+308. Size - 8 byte
string – Text(UTF8).Each letter is 2 bytes, 1st - the code of letter, 2nd –
number of code table. The end of line is 0 symbol
 */

abstract class WriteablePacket : SockPacket() {
    abstract val opCode: Byte
    private var buffer: ByteBuffer? = null

    protected abstract fun write()

    fun writeInto(buf: ByteBuffer) {
        buffer = buf
        write()
        buffer = null
    }

    protected fun writeC(data: Byte) {
        buffer?.put(data)
    }

    protected fun writeC(data: Int) {
        buffer?.put(data.toByte())
    }

    protected fun writeF(value: Double) {
        buffer?.putDouble(value)
    }

    protected fun writeH(value: Int) {
        buffer?.putShort(value.toShort())
    }

    protected fun writeD(value: Int) {
        buffer?.putInt(value)
    }

    protected fun writeQ(value: Long) {
        buffer?.putLong(value)
    }

    protected fun writeB(data: ByteArray) {
        buffer?.put(data)
    }

    protected fun writeS(text: String?) {
        if (text != null) {
            val len = text.length
            for (i in 0 until len) {
                buffer?.putChar(text[i])
            }
        }
        buffer?.putChar('\u0000')
    }
}

abstract class ReadablePacket : SockPacket() {
    private var buffer: ByteBuffer? = null
    private var sBuffer: StringBuffer? = null

    abstract fun read()

    fun readFromBuffer(buf: ByteBuffer, tempStringBuff: StringBuffer) {
        buffer = buf
        sBuffer = tempStringBuff
        read()
        buffer = null
        sBuffer = null
    }

    protected fun readB(dst: ByteArray?) {
        buffer!!.get(dst)
    }

    protected fun readC(): Int {
        return buffer!!.get().toInt() and 0xFF
    }

    protected fun readH(): Int {
        return buffer!!.short.toInt() and 0xFFFF
    }

    protected fun readD(): Int {
        return buffer!!.int
    }

    protected fun readS(): String {
        sBuffer!!.setLength(0)

        while (true) {
            val ch = buffer?.char
            if (ch != null && ch.code.toByte() != 0.toByte()) {
                sBuffer?.append(ch)
            } else {
                break
            }
        }

        return sBuffer.toString()
    }
}