package lineage.vetal.server.login.gameserver.packet.client

import lineage.vetal.server.core.model.SessionKey
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.game.GameContext
import lineage.vetal.server.login.gameserver.GameClient
import lineage.vetal.server.login.gameserver.packet.GamePacket
import lineage.vetal.server.login.gameserver.packet.server.CharSlotList

class AuthLogin : GamePacket() {
    private val TAG = "AuthLogin"
    lateinit var account: String
    var playKey1 = 0
    var playKey2 = 0
    var loginKey1 = 0
    var loginKey2 = 0

    override fun execute(client: GameClient, context: GameContext) {
        // TODO validate it via bridge server communication
        writeInfo(TAG, "Account connected $account")
        val account = context.gameDatabase.accountDao.findAccount(account)
        if (account == null) {
            client.saveAndClose()
            return
        }

        val sessionKey = SessionKey(playKey1, playKey2, loginKey1, loginKey1)
        client.sessionKey = sessionKey
        client.account = account

        val slots = context.gameDatabase.charactersDao.getCharSlots(client.account.id)
        client.characterSlots = slots
        client.sendPacket(CharSlotList(client, slots))
    }

    override fun read() {
        account = readS().lowercase()
        playKey2 = readD()
        playKey1 = readD()
        loginKey1 = readD()
        loginKey2 = readD()
    }
}