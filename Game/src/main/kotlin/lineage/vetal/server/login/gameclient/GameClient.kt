package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.core.utils.ext.ifNullOrBlank
import lineage.vetal.server.login.game.model.CharSelectionSlot
import lineage.vetal.server.login.game.model.player.Player
import vetal.server.network.Client
import vetal.server.network.SendablePacket
import vetal.server.writeDebug


class GameClient(
    override val connection: GameClientConnection,
) : Client() {
    private val TAG = "GameClient"
    lateinit var account: AccountInfo
    lateinit var sessionKey: SessionKey
    var clientState = GameClientState.LOBBY
    var sessionId: Int = -1

    var characterSlots: List<CharSelectionSlot>? = null
    var player: Player? = null

    fun sendInitPacket() {
        connection.sendInitPacket()
    }

    override fun sendPacket(packet: SendablePacket) {
        super.sendPacket(packet)
        writeDebug(TAG, "`${packet::class.java.simpleName}` -> ${player?.name?.ifNullOrBlank { account.account }}")
    }
}

enum class GameClientState {
    LOBBY, LOADING, WORLD
}