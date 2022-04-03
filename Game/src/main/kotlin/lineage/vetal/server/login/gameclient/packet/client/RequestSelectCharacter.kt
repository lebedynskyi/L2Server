package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.packet.GamePacket
import lineage.vetal.server.login.gameclient.packet.server.CharSelected
import lineage.vetal.server.login.gameclient.packet.server.SSQInfo

class RequestSelectCharacter : GamePacket() {
    private val TAG = "RequestSelectCharacter"
    private var slotIndex = -1

    override fun execute(client: GameClient, context: GameContext) {
        val slot = client.characterSlots?.getOrNull(slotIndex)
        if (slot == null) {
            writeDebug(TAG, "Unable to select character for account ${client.account.account}")
            client.saveAndClose()
            return
        }

        if (client.player != null){
            writeDebug(TAG, "Unable to select character for account ${client.account.account}. Player is already attached")
            return
        }

        val player = context.gameDatabase.charactersDao.getCharacter(slot.id)
        if (player == null) {
            writeDebug(TAG, "Unable to select character for account ${client.account.account}. Cannot find player")
            client.saveAndClose()
            return
        }

        client.player = player

        client.sendPacket(SSQInfo.RED_SKY_PACKET)
        client.sendPacket(CharSelected(player, client.sessionKey.playOkID1))
    }

    override fun read() {
        slotIndex = readD()
        readH() // Not used.
        readD() // Not used.
        readD() // Not used.
        readD() // Not used.
    }
}