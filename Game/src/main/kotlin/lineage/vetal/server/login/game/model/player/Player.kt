package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.game.model.player.status.PlayerStatus
import lineage.vetal.server.login.game.model.template.CharacterTemplate
import java.util.UUID

class Player(
    id: UUID,
    name: String,
    val accountId: UUID,
    val charTemplate: CharacterTemplate,
    val appearance: Appearance,
    val position: SpawnLocation
) : Playable(id, name) {
    override var status: PlayerStatus = PlayerStatus(charTemplate)

    val race get() = charTemplate.classId.race.ordinal
    val classId get() = charTemplate.classId.ordinal
    var baseClassId = charTemplate.classId.id

    var karma: Int = 0
    var pvpKills: Int = 0
    var pkKills: Int = 0
    var clanId: Int = 0
    var deleteTimer = 0L
    var hasDwarvenCraft = false
    var title: String? = "I Did it!"
    var accessLevel = 0
    var isOnlineInt = 0
    var isIn7sDungeon = false
    var clanPrivileges = 0
    var wantsPeace = false
    var isNoble = false
}
