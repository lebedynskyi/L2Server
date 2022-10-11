package lineage.vetal.server.login.gameserver.packet

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import vetal.server.network.ReceivablePacket

abstract class GamePacket : ReceivablePacket() {
    abstract fun execute(client: GameClient, context: GameContext)
}