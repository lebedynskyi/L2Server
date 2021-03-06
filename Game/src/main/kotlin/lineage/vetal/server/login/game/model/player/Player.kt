package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.login.game.model.items.Inventory
import lineage.vetal.server.login.game.model.player.status.PlayerStatus
import lineage.vetal.server.login.game.model.template.CharTemplate
import lineage.vetal.server.login.gameclient.GameClient
import lineage.vetal.server.login.gameclient.packet.server.MoveToLocation
import java.util.*

class Player(
    id: UUID,
    name: String,
    val accountId: UUID,
    val charTemplate: CharTemplate,
    val appearance: Appearance,
    position: SpawnLocation
) : Playable(id, name, position) {
    var client: GameClient? = null

    override var status: PlayerStatus = PlayerStatus(charTemplate)
    lateinit var inventory: Inventory

    var summon: Summon? = null
    var team: TeamType = TeamType.NONE
    val operateType: OperateType = OperateType.NONE

    val raceId get() = charTemplate.charClass.race.ordinal
    val classId get() = charTemplate.charClass.ordinal
    var baseClassId = charTemplate.charClass.id

    var isGM = false
    var accessLevel = 0
    var deleteTimer = 0L

    var karma: Int = 0
    var pvpKills: Int = 0
    var pvpFlag: Int = 0
    var pkKills: Int = 0

    var clanId: Int = 0
    var clanCrestId: Int = 0
    var allyId: Int = 0
    var allyCrestId: Int = 0
    var clanPrivelegies: Int = 0

    var isOnlineInt = 0
    var isIn7sDungeon = false
    var isInPartyMatchRoom = false
    var clanPrivileges = 0
    var isNoble = false
    var isHero = false
    var isFishing = false
    var isMounted = false
    var mountType: Int = 0

    var recomLeft: Int = 0
    var recomHave: Int = 0
    var mountNpcId: Int = 0
    var hasDwarvenCraft = false
    var wantsPeace = false
    var enchantEffect: Int = 0
    var pledgeType = 0
    var pledgeClass = 0

    var isSitting: Boolean = false

    init {
        isRunning = true
    }

    fun getCollisionRadius(): Double {
        return charTemplate.getCollisionRadiusBySex(appearance.sex)
    }

    fun getCollisionHeight(): Double {
        return charTemplate.getCollisionHeightBySex(appearance.sex)
    }

    fun sendPacket(packet: SendablePacket) {
        client?.sendPacket(packet)
    }
}
