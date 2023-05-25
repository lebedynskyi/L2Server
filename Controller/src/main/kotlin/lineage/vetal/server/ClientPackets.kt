package lineage.vetal.server

import vetal.server.sock.ReadablePacket

class RequestAuth : ReadablePacket() {
    var isController: Boolean = true

    override fun read() {
        isController = readC() > 0
    }

    override fun toString(): String {
        return "RequestAuth(isController=$isController)"
    }
}

class RequestAction : ReadablePacket() {
    var speed: Int = 0
    var topPressed: Boolean = false
    var leftPressed: Boolean = false
    var rightPressed: Boolean = false
    var bottomPressed: Boolean = false

    override fun read() {
        speed = readD()
        topPressed = readC() > 0
        leftPressed = readC() > 0
        rightPressed = readC() > 0
        bottomPressed = readC() > 0
    }

    override fun toString(): String {
        return "RequestAction(speed=$speed, topPressed=$topPressed, leftPressed=$leftPressed, rightPressed=$rightPressed, bottomPressed=$bottomPressed)"
    }
}