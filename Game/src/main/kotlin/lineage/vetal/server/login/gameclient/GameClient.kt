package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.login.game.model.CharSelectionSlot
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.game.model.player.status.PlayerStatus



class GameClient(
    override val connection: GameClientConnection,
) : Client() {
    lateinit var account: AccountInfo
    lateinit var sessionKey: SessionKey
    var clientState = GameClientState.LOBBY
    var sessionId: Int = -1

    var characterSlots: List<CharSelectionSlot>? = null
    var player: Player? = null

    fun sendInitPacket() {
        connection.sendInitPacket()
    }
}

enum class GameClientState {
    LOBBY, LOADING, WORLD
}