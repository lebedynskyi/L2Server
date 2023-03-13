package lineage.vetal.server.login.bridgeclient.packets.server

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.model.RegisteredServer
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.login.bridgeclient.packets.BridgeGamePacket
import lineage.vetal.server.login.bridgeclient.packets.client.RequestInit
import lineage.vetal.server.login.game.GameContext

class BridgeConnected : BridgeGamePacket() {
    override fun read() {

    }

    override fun execute(client: BridgeClient, context: GameContext) {
        context.gameLobby.onServerConnected(client)
    }
}