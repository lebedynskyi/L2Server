package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.login.game.model.player.Creature
import lineage.vetal.server.login.game.model.player.SayType
import lineage.vetal.server.login.gameclient.packet.GameServerPacket
import java.sql.ResultSet
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

    constructor(rs: ResultSet) : this(
        rs.getInt("player_oid"),
        SayType.valueOf(rs.getString("type")),
        rs.getString("player_name"),
        rs.getString("content")
    )

    constructor(type: SayType, name: String?, content: String?) : this(0, type, name, content) {}

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