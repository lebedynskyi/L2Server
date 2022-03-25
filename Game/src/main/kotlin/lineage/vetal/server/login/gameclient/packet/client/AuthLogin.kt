package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.packet.GamePacket
import lineage.vetal.server.login.gameclient.packet.server.CharSlotList

class AuthLogin : GamePacket() {
    private val TAG = "AuthLogin"
    lateinit var loginName: String
    var playKey1 = 0
    var playKey2 = 0
    var loginKey1 = 0
    var loginKey2 = 0

    override fun execute(client: GameClient, context: GameContext) {
        writeInfo(TAG, "Account connected $loginName")
        val sessionKey = SessionKey(playKey1, playKey2, loginKey1, loginKey1)
        client.sessionKey = sessionKey
        client.account = AccountInfo(loginName)

        // TODO check login server info about account
        client.sendPacket(CharSlotList(client, emptyList()))
    }

    override fun read() {
        loginName = readS().lowercase()
        playKey2 = readD()
        playKey1 = readD()
        loginKey1 = readD()
        loginKey2 = readD()
    }
}