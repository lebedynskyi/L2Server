package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket
import lineage.vetal.server.login.gameserver.packet.server.NewCharacterSuccess

class RequestCharacterTemplates : GamePacket() {
    override fun execute(client: GameClient, context: GameContext) {
        client.sendPacket(NewCharacterSuccess(emptyList()))
    }

    override fun read() {

    }
}