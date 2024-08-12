package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket

class RequestItemList: GamePacket() {
    override fun executeImpl(client: GameClient, context: GameContext) {
        writeDebug("RequestItemList", "Not implemented")
    }
}