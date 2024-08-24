package lineage.vetal.server.game.game.model.player

import lineage.vetal.server.game.game.model.behaviour.PlayerBehaviour
import lineage.vetal.server.game.game.model.inventory.WearableInventory
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.player.status.PlayerStatus
import lineage.vetal.server.game.game.model.template.pc.CharTemplate
import lineage.vetal.server.game.gameserver.GameClient
import vetalll.server.sock.WriteablePacket


class PlayerObject(
    objectId: Int,
    val id: String,
    val accountId: String,
    val appearance: Appearance,
    private var charName: String,
    override val template: CharTemplate,
    override var position: SpawnPosition,
    override val behaviour: PlayerBehaviour = PlayerBehaviour(),
) : Playable(objectId, position, template, behaviour) {
    lateinit var inventory: WearableInventory

    override var stats: PlayerStatus = PlayerStatus(template)

    override val name get() = charName // TODO if (player.getPolymorphTemplate() != null) player.getPolymorphTemplate().getName() else player.getName()

    var client: GameClient? = null

    var team: TeamType = TeamType.NONE
    val operateType: OperateType = OperateType.NONE
    var isInWorld = false
    var lastAccessTime: Long = 0
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

    var isOnlineInt = 0
    var isIn7sDungeon = false
    var isInPartyMatchRoom = false
    var clanPrivileges = 0

    var isNoble = false
    var isHero = false

    var recLeft: Int = 0
    var recHave: Int = 0

    var hasDwarvesCraft = false
    var wantsPeace = false
    var enchantEffect: Int = 0
    var pledgeType = 0
    var pledgeClass = 0

    var summon: Summon? = null
    var target: CreatureObject? = null

    fun getCollisionRadius(): Double {
        return template.getCollisionRadiusBySex(appearance.sex)
    }

    fun getCollisionHeight(): Double {
        return template.getCollisionHeightBySex(appearance.sex)
    }

    fun sendPacket(packet: WriteablePacket) {
        client?.sendPacket(packet)
    }

    fun sendPacket(packets: List<WriteablePacket>) {
        packets.forEach {
            client?.sendPacket(it)
        }
    }

    override fun toString(): String {
        return "$objectId - $name"
    }
}