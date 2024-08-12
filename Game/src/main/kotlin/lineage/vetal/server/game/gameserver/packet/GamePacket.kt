package lineage.vetal.server.game.gameserver.packet

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.gameserver.GameClient
import vetalll.server.sock.ReadablePacket

private const val TAG = "GamePacket"

abstract class GamePacket : ReadablePacket() {
    override fun read() {}

    fun execute(client: GameClient, context: GameContext) {
        try {
            executeImpl(client, context)
        } catch (e: Exception) {
            writeError(TAG, "Disconnect Player. Unable to execute packet '${this::class.java.simpleName}'", e)
            client.saveAndClose()
        }
    }

    protected abstract fun executeImpl(client: GameClient, context: GameContext)
}