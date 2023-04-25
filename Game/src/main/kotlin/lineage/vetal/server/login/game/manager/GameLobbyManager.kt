package lineage.vetal.server.login.game.manager

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.model.RegisteredServer
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.core.utils.ext.isValidPlayerName
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.ConfigGame
import lineage.vetal.server.login.bridgeclient.packets.client.RequestAuth
import lineage.vetal.server.login.bridgeclient.packets.client.RequestInit
import lineage.vetal.server.login.db.GameDatabase
import lineage.vetal.server.login.game.GameObjectFactory
import lineage.vetal.server.login.game.model.template.pc.CharTemplate
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.GameClientState
import lineage.vetal.server.login.gameserver.packet.server.*
import vetal.server.writeError

private const val TAG = "GameLobby"

class GameLobbyManager(
    private val gameConfig: ConfigGame,
    private val gameWorld: WorldManager,
    private val gameDatabase: GameDatabase,
    private val objectFactory: GameObjectFactory,
    private val charStatsData: Map<Int, CharTemplate>,
) {
    // TODO pending auth clients should be stored here.. And cleared after timer
    fun onConnectedToBridge(client: BridgeClient) {
        val serverConfig = gameConfig.serverInfo
        val blowFishKey = serverConfig.bridgeKey
        val serverStatus = ServerStatus(
            gameConfig.serverInfo.id,
            gameWorld.players.size,
            true,
            gameConfig.serverInfo.ip
        )

        client.connection.crypt.init(blowFishKey.toByteArray())
        client.serverInfo = RegisteredServer(serverConfig, serverStatus)
        client.sendPacket(RequestInit(gameConfig.serverInfo.id))
    }

    fun onBridgeInitOk(client: BridgeClient) {
        val serverStatus = client.serverInfo?.status
        if (serverStatus == null) {
            client.saveAndClose()
            writeError("InitOK", "Status is null. Close connection")
            return
        }

        client.sendPacket(RequestAuth(serverStatus))
    }

    fun requestAuthLogin(client: GameClient, account: String, loginKey1: Int, loginKey2: Int, playKey1: Int, playKey2: Int) {
        // TODO validate it via bridge server communication.
        writeInfo(TAG, "Account connected $account")
        val accountInfo = gameDatabase.accountDao.findAccount(account)
        if (accountInfo == null) {
            client.saveAndClose()
            return
        }

        val sessionKey = SessionKey(playKey1, playKey2, loginKey1, loginKey1)
        client.sessionKey = sessionKey
        client.account = accountInfo

        val slots = gameDatabase.charactersDao.getCharSlots(client.account.id)
        client.characterSlots = slots
        client.sendPacket(CharSlotList(client, slots))
    }

    fun requestCreateChar(
        client: GameClient,
        name: String,
        classId: Int,
        race: Int,
        sex: Byte,
        hairStyle: Int,
        hairColor: Int,
        face: Int
    ) {
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

        // Invalid hairstyle.
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
        /*
        // Your name is already taken by a NPC.
        if (NpcData.getInstance().getTemplateByName(_name) != null) {
            sendPacket(CharCreateFail.REASON_INCORRECT_NAME)
            return
        }

        // You already have the maximum amount of characters for this account.
        if (PlayerInfoTable.getInstance().getCharactersInAcc(getClient().getAccountName()) >= 7) {
            sendPacket(CharCreateFail.REASON_TOO_MANY_CHARACTERS)
            return
        }

        // The name already exists.
        if (PlayerInfoTable.getInstance().getPlayerObjectId(_name) > 0) {
            sendPacket(CharCreateFail.REASON_NAME_ALREADY_EXISTS)
            return
        }

        // The class id related to this template is post-newbie.
        val template: PlayerTemplate = PlayerData.getInstance().getTemplate(_classId)
        if (template == null || template.getClassBaseLevel() > 1) {
            sendPacket(CharCreateFail.REASON_CREATION_FAILED)
            return
        }

        // Create the player Object.
        val player: Player = Player.create(
            IdFactory.getInstance().getNextId(),
            template,
            getClient().getAccountName(),
            _name,
            _hairStyle,
            _hairColor,
            _face,
            Sex.VALUES.get(_sex.toInt())
        )
        if (player == null) {
            sendPacket(CharCreateFail.REASON_CREATION_FAILED)
            return
        }
*/

        // The class id related to this template is post-newbie.
        val playerTemplate = charStatsData[classId]
        if (playerTemplate == null || playerTemplate.classBaseLevel > 1) {
            client.sendPacket(CreateCharFail.REASON_CREATION_FAILED)
            return
        }

        val newPlayer = objectFactory.createPlayerObject(name, client.account, playerTemplate, hairStyle, hairColor, face, sex)
        gameDatabase.charactersDao.insertCharacter(newPlayer)
        gameDatabase.itemsDao.saveInventory(newPlayer.inventory.items)

        val slots = gameDatabase.charactersDao.getCharSlots(client.account.id)
        client.sendPacket(CreateCharOK.STATIC_PACKET)

        val charSelectInfo = CharSlotList(client, slots)
        client.sendPacket(charSelectInfo)

        client.characterSlots = slots
    }

    fun requestSelectChar(client: GameClient, slotIndex: Int) {
        val slot = client.characterSlots.getOrNull(slotIndex)
        if (slot == null) {
            writeDebug(TAG, "Unable to select character for account ${client.account.account}")
            client.saveAndClose()
            return
        }

        if (client.player != null){
            writeDebug(TAG, "Unable to select character for account ${client.account.account}. Player is already attached")
            return
        }

        val playerItems = gameDatabase.itemsDao.getInventoryForPlayer(slot.id)
        val player = gameDatabase.charactersDao.getCharacter(slot.id)?.apply {
            inventory.addAll(playerItems)
        }

        if (player == null) {
            writeDebug(TAG, "Unable to select character for account ${client.account.account}. Cannot find player")
            client.saveAndClose()
            return
        }

        client.clientState = GameClientState.LOADING
        client.player = player
        client.sendPacket(SSQInfo.REGULAR_SKY_PACKET)
        client.sendPacket(CharSelected(player, client.sessionKey.playOkID1))
    }
}