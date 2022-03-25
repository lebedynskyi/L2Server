package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey

class GameClient(
    override val connection: GameClientConnection
) : Client() {
    var clientState = GameClientState.LOBBY
    var account: AccountInfo? = null
    var sessionKey: SessionKey? = null

    fun sendInitPacket() {
        connection.sendInitPacket()
    }
}

enum class GameClientState {
    LOBBY, LOADING, WORLD
}