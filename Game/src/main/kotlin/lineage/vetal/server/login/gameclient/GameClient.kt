package lineage.vetal.server.login.gameclient

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey

class GameClient(
    override val connection: GameClientConnection
) : Client() {
    var clientState = GameClientState.LOBBY
    lateinit var account: AccountInfo
    lateinit var sessionKey: SessionKey
    var sessionId: Int = -1

    fun sendInitPacket() {
        connection.sendInitPacket()
    }
}

enum class GameClientState {
    LOBBY, LOADING, WORLD
}