package lineage.vetal.server.login.gameserver

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.login.game.model.CharSelectionSlot
import lineage.vetal.server.login.game.model.player.Player
import vetal.server.network.Client

private const val TAG = "GameClient"

class GameClient(
    override val connection: GameClientConnection,
) : Client() {

    lateinit var account: AccountInfo
    lateinit var sessionKey: SessionKey
    lateinit var characterSlots: List<CharSelectionSlot>

    var clientState = GameClientState.LOBBY
    var player: Player? = null

    fun sendInitPacket() {
        connection.sendInitPacket()
    }
}

enum class GameClientState {
    LOBBY, LOADING, WORLD
}