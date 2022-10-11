package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.player.SayType
import lineage.vetal.server.login.gameserver.packet.GameServerPacket
import kotlin.Int
import kotlin.String

class CreatureSay : GameServerPacket {
    val objectId: Int
    var name: String? = null
    var content: String? = null
    private val sayType: SayType
    private var sysStringId = 0// from sysstring-e.dat
    private var sysMsgId = 0 // from systemmsg-e.dat

    constructor(creature: Creature, sayType: SayType, content: String?) : this(
        creature.objectId,
        sayType,
        creature.name,
        content
    )

    constructor(objectId: Int, sayType: SayType, name: String?, content: String?) {
        this.objectId = objectId
        this.sayType = sayType
        this.name = name
        this.content = content
    }
//
//    constructor(sayType: SayType, sysStringId: Int, sysMsgId: SystemMessageId) {
//        objectId = 0
//        _sayType = sayType
//        _sysStringId = sysStringId
//        _sysMsgId = sysMsgId.getId()
//    }

    override fun write() {
        writeC(0x4a)
        writeD(objectId)
        writeD(sayType.ordinal)
        if (content != null) {
            writeS(name)
            writeS(content)
        } else {
            writeD(sysStringId)
            writeD(sysMsgId)
        }
    }
}