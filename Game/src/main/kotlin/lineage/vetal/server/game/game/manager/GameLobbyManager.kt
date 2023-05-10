package lineage.vetal.server.game.game.manager

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.model.RegisteredServer
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.core.utils.ext.isValidPlayerName
import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.bridgeclient.packets.client.RequestAuth
import lineage.vetal.server.game.bridgeclient.packets.client.RequestInit
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.GameClientState
import lineage.vetal.server.game.gameserver.packet.server.*
import vetal.server.writeError

private const val TAG = "GameLobby"

class GameLobbyManager(
    private val context: GameContext,
) {
    // TODO pending auth clients should be stored here.. And cleared after timer
    fun onConnectedToBridge(client: BridgeClient) {
        val serverConfig = context.gameConfig.serverInfo
        val blowFishKey = serverConfig.bridgeKey
        val serverStatus = ServerStatus(
            context.gameConfig.serverInfo.id,
            context.worldManager.players.size,
            true,
            context.gameConfig.serverInfo.ip
        )

        client.connection.crypt.init(blowFishKey.toByteArray())
        client.serverInfo = RegisteredServer(serverConfig, serverStatus)
        client.sendPacket(RequestInit(context.gameConfig.serverInfo.id))
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
        val accountInfo = context.gameDatabase.accountDao.findAccount(account)
        if (accountInfo == null) {
            client.saveAndClose()
            return
        }

        val sessionKey = SessionKey(playKey1, playKey2, loginKey1, loginKey1)
        client.sessionKey = sessionKey
        client.account = accountInfo
        onCharSlotSelection(client)
    }

    fun onCharSlotSelection(client: GameClient) {
        val slots = context.gameDatabase.charactersDao.getCharSlots(client.account.id)
        var temp = 0L
        var lastActiveIndex = -1
        slots.forEachIndexed{ index, slot ->
            if (slot.lastAccess >= temp) {
                lastActiveIndex = index
                temp = slot.lastAccess
            }
        }
        client.characterSlots = slots
        client.sendPacket(CharSlotList(client, slots, lastActiveIndex))
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
*/
        val newPlayer = context.objectFactory.createPlayerObject(name, client.account, classId, hairStyle, hairColor, face, sex, true)
        context.gameDatabase.charactersDao.insertCharacter(newPlayer)
        context.gameDatabase.itemsDao.saveInventory(newPlayer.inventory.items)
        client.sendPacket(CreateCharOK.STATIC_PACKET)
        onCharSlotSelection(client)
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

        val playerItems = context.gameDatabase.itemsDao.getInventoryForPlayer(slot.id)
        val player = context.gameDatabase.charactersDao.getCharacter(slot.id)?.apply {
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