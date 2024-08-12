package lineage.vetal.server.game.gameserver.packet.client

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.packet.GamePacket
import lineage.vetal.server.game.gameserver.packet.server.NewCharacterSuccess

// TODO empty packet.. Depends on client how it handles it.
class RequestCharacterTemplates : GamePacket() {
    override fun read() {

    }

    override fun executeImpl(client: GameClient, context: GameContext) {
        client.sendPacket(NewCharacterSuccess(emptyList()))
    }
}