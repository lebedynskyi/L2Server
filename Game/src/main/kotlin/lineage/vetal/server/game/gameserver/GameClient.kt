package lineage.vetal.server.game.gameserver

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.game.game.model.CharSelectionSlot
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.server.ServerClose
import vetalll.server.sock.SockClient
import vetalll.server.sock.WriteablePacket

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

    override fun saveAndClose(reason: WriteablePacket?) {
        if (reason == null) {
            super.saveAndClose(ServerClose.STATIC_PACKET)
        } else {
            super.saveAndClose(reason)
        }
    }
}

enum class GameClientState {
    LOBBY, LOADING, WORLD
}