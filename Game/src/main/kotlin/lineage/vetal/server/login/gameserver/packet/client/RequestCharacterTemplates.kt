package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket
import lineage.vetal.server.login.gameserver.packet.server.NewCharacterSuccess

// TODO empty packet.. Depends on client how it handles it.
class RequestCharacterTemplates : GamePacket() {
    override fun read() {

    }

    override fun execute(client: GameClient, context: GameContext) {
        client.sendPacket(NewCharacterSuccess(emptyList()))
    }
}