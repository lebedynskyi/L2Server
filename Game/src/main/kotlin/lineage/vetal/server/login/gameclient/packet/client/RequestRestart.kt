package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.GameClientState
import lineage.vetal.server.login.gameclient.packet.GamePacket
import lineage.vetal.server.login.gameclient.packet.server.CharSlotList
import lineage.vetal.server.login.gameclient.packet.server.RestartResponse

class RequestRestart : GamePacket() {
    override fun execute(client: GameClient, context: GameContext) {
        val player = client.player ?: return

        client.sendPacket(RestartResponse.STATIC_PACKET_OK)

        val slots = context.gameDatabase.charactersDao.getCharSlots(client.account.id)
        client.characterSlots = slots
        client.player = null
        client.clientState = GameClientState.LOBBY
        client.sendPacket(CharSlotList(client, slots))

        context.gameWorld.decay(player)
    }

    override fun read() {

    }
}