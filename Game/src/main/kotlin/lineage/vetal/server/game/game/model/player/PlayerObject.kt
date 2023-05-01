package lineage.vetal.server.game.game.model.player

import lineage.vetal.server.game.game.model.inventory.WearableInventory
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.player.status.PlayerStatus
import lineage.vetal.server.game.game.model.template.pc.CharTemplate
import lineage.vetal.server.game.gameserver.GameClient
import vetal.server.network.SendablePacket

class PlayerObject(
    val id: String,
    val accountId: String,
    objectId: Int,
    name: String,
    val charTemplate: CharTemplate,
    val appearance: Appearance,
    position: SpawnPosition
) : Playable(objectId, name, position) {
    override var stats: PlayerStatus = PlayerStatus(charTemplate)
    var inventory: WearableInventory = WearableInventory()

    var lastAccessTime: Long = 0
    var client: GameClient? = null
    var isActive = false
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

    fun sendPacket(packets: List<SendablePacket>) {
        packets.forEach {
            client?.sendPacket(it)
        }
    }
}
