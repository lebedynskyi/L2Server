package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket


class RequestCreateCharacter : GamePacket() {
    lateinit var name: String
    var race = -1
    var sex: Byte = -1
    var classId = -1
    var int = -1
    var str = -1
    var con = -1
    var men = -1
    var dex = -1
    var wit = -1
    var hairStyle: Int = -1
    var hairColor: Int = -1
    var face: Int = -1

    override fun read() {
        name = readS()
        race = readD()
        sex = readD().toByte()
        classId = readD()
        int = readD()
        str = readD()
        con = readD()
        men = readD()
        dex = readD()
        wit = readD()
        hairStyle = readD()
        hairColor = readD()
        face = readD()
    }

    override fun execute(client: GameClient, context: GameContext) {
        context.gameLobby.requestCreateChar(client, name, classId, race, sex, hairStyle, hairColor, face)
    }
}