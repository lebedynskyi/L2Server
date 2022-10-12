package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.core.utils.ext.isValidPlayerName
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.game.model.player.Sex
import lineage.vetal.server.login.game.model.player.Appearance
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket
import lineage.vetal.server.login.gameserver.packet.server.CharSlotList
import lineage.vetal.server.login.gameserver.packet.server.CreateCharFail
import lineage.vetal.server.login.gameserver.packet.server.CreateCharOK
import java.util.*


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
    var hairStyle: Int = -1
    var hairColor: Int = -1
    var face: Int = -1

    override fun execute(client: GameClient, context: GameContext) {
        writeDebug(TAG, "Request to create char with name `$name`")

        // Invalid race.
        if (race > 4 || race < 0) {
            client.sendPacket(CreateCharFail.REASON_CREATION_FAILED)
            return
        }

        // Invalid face.
        if (face > 2 || face < 0) {
            client.sendPacket(CreateCharFail.REASON_CREATION_FAILED)
            return
        }

        // Invalid hair style.
        if (hairStyle < 0 || sex.toInt() == 0 && hairStyle > 4 || sex.toInt() != 0 && hairStyle > 6) {
            client.sendPacket(CreateCharFail.REASON_CREATION_FAILED)
            return
        }

        // Invalid hair color.
        if (hairColor > 3 || hairColor < 0) {
            client.sendPacket(CreateCharFail.REASON_CREATION_FAILED)
            return
        }

        // Invalid name typo.
        if (!name.isValidPlayerName()) {
            client.sendPacket(CreateCharFail.REASON_INCORRECT_NAME)
            return
        }

//        // Your name is already taken by a NPC.
//        if (NpcData.getInstance().getTemplateByName(_name) != null) {
//            sendPacket(CharCreateFail.REASON_INCORRECT_NAME)
//            return
//        }
//
//        // You already have the maximum amount of characters for this account.
//        if (PlayerInfoTable.getInstance().getCharactersInAcc(getClient().getAccountName()) >= 7) {
//            sendPacket(CharCreateFail.REASON_TOO_MANY_CHARACTERS)
//            return
//        }
//
//        // The name already exists.
//        if (PlayerInfoTable.getInstance().getPlayerObjectId(_name) > 0) {
//            sendPacket(CharCreateFail.REASON_NAME_ALREADY_EXISTS)
//            return
//        }
//
//        // The class id related to this template is post-newbie.
//        val template: PlayerTemplate = PlayerData.getInstance().getTemplate(_classId)
//        if (template == null || template.getClassBaseLevel() > 1) {
//            sendPacket(CharCreateFail.REASON_CREATION_FAILED)
//            return
//        }
//
//        // Create the player Object.
//        val player: Player = Player.create(
//            IdFactory.getInstance().getNextId(),
//            template,
//            getClient().getAccountName(),
//            _name,
//            _hairStyle,
//            _hairColor,
//            _face,
//            Sex.VALUES.get(_sex.toInt())
//        )
//        if (player == null) {
//            sendPacket(CharCreateFail.REASON_CREATION_FAILED)
//            return
//        }


        // The class id related to this template is post-newbie.
        val playerTemplate = context.charStatsData[classId]
        if (playerTemplate == null || playerTemplate.classBaseLevel > 1) {
            client.sendPacket(CreateCharFail.REASON_CREATION_FAILED)
            return
        }

        val newPlayer = Player(
            UUID.randomUUID(),
            name,
            client.account.id,
            playerTemplate,
            Appearance(hairStyle, hairColor, face, Sex.values()[sex.toInt()]),
            SpawnLocation(playerTemplate.randomSpawn)
        )

        context.gameDatabase.charactersDao.insertCharacter(newPlayer)

        val slots = context.gameDatabase.charactersDao.getCharSlots(client.account.id)
        client.sendPacket(CreateCharOK.STATIC_PACKET)

        val charSelectInfo = CharSlotList(client, slots)
        client.sendPacket(charSelectInfo)

        client.characterSlots = slots
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
        hairStyle = readD()
        hairColor = readD()
        face = readD()
    }
}