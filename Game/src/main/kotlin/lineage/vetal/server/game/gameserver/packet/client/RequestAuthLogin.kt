package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestAuthLogin : GamePacket() {
    lateinit var account: String
    var playKey1 = 0
    var playKey2 = 0
    var loginKey1 = 0
    var loginKey2 = 0

    override fun read() {
        account = readS().lowercase()
        playKey2 = readD()
        playKey1 = readD()
        loginKey1 = readD()
        loginKey2 = readD()
    }
}