package lineage.vetal.server.login.gameclient.packet

import lineage.vetal.server.core.server.ReceivablePacket
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient

abstract class GamePacket : ReceivablePacket() {
    abstract fun execute(client: GameClient, context: GameContext)
}