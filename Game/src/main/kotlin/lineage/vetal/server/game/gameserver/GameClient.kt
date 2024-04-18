package lineage.vetal.server.game.gameserver

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.game.game.model.CharSelectionSlot
import lineage.vetal.server.game.game.model.player.PlayerObject
import vetalll.server.sock.SockClient

private const val TAG = "GameClient"

class GameClient(
    override val connection: GameClientConnection,
) : SockClient(connection) {

    lateinit var account: AccountInfo
    lateinit var sessionKey: SessionKey
    lateinit var characterSlots: List<CharSelectionSlot>

    var clientState = GameClientState.LOBBY
    var player: PlayerObject? = null

    fun sendInitPacket() {
        connection.sendInitPacket()
    }
}

enum class GameClientState {
    LOBBY, LOADING, WORLD
}