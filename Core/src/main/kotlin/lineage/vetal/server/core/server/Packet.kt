package lineage.vetal.server.core.server

abstract class Packet

class ReceivablePacket : Packet() {
    fun read() {}
}

class SendablePacket : Packet() {
    fun write() {}
}