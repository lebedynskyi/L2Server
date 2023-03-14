package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.GameClientState
import lineage.vetal.server.login.gameserver.packet.GamePacket
import lineage.vetal.server.login.gameserver.packet.server.CharSlotList
import lineage.vetal.server.login.gameserver.packet.server.RestartResponse

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
        context.gameDatabase.charactersDao.updateCoordinates(player.id, player.position)
    }

    override fun read() {

    }
}