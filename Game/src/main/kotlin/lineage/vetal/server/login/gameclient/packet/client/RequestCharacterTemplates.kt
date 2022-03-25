package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.packet.GamePacket
import lineage.vetal.server.login.gameclient.packet.server.CharTemplates

class RequestCharacterTemplates : GamePacket() {
    override fun execute(client: GameClient, context: GameContext) {
        client.sendPacket(CharTemplates(emptyList()))
    }

    override fun read() {

    }
}