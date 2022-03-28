package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.core.utils.ext.isValidPlayerName
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.packet.GamePacket
import lineage.vetal.server.login.gameclient.packet.server.CharSlotList
import lineage.vetal.server.login.gameclient.packet.server.CreateCharFail
import lineage.vetal.server.login.gameclient.packet.server.CreateCharOK


class RequestCreateCharacter : GamePacket() {
    private val TAG = "RequestCreateCharacter"

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
    var hairStyle: Byte = -1
    var hairColor: Byte = -1
    var face: Byte = -1

    override fun execute(client: GameClient, context: GameContext) {
        writeDebug(TAG, "Request to create char with name `$name`")

        if (!name.isValidPlayerName() || name.lowercase().startsWith("gm")) {
            client.sendPacket(CreateCharFail.REASON_INCORRECT_NAME)
        } else {
            client.sendPacket(CreateCharOK.STATIC_PACKET)
            val charSelectInfo = CharSlotList(client, emptyList())
            client.sendPacket(charSelectInfo)
        }
    }

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
        hairStyle = readD().toByte()
        hairColor = readD().toByte()
        face = readD().toByte()
    }
}