package lineage.vetal.server.login.gameclient.packet.client

import lineage.vetal.server.login.GameContext
import lineage.vetal.server.login.game.model.player.SayType
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.packet.GamePacket
import lineage.vetal.server.login.gameclient.packet.server.CreatureSay

class Say2 : GamePacket() {
    lateinit var text: String
    var typeId: Int = -1
    var targetName: String? = null

    override fun execute(client: GameClient, context: GameContext) {
        val player = client.player ?: return

        if (typeId < 0 || typeId >= SayType.values().size) return
        if (text.isEmpty() || text.length > 100) return

        //Check player role. Chat ban ? Player is gm ? and etc etc

        val sayType = SayType.values()[typeId]
        text = text.replace("\\\\n", "")

        //  check message type. Check command ?

        // Where to put logic ? All logics in packets? What if player impacts on world?
        context.gameWorld.broadCastPacket(CreatureSay(player, sayType, text))
    }

    override fun read() {
        text = readS()
        typeId = readD()
        targetName = if (typeId == SayType.TELL.ordinal) readS() else null
    }
}