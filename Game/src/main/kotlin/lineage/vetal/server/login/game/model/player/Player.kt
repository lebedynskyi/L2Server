package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.game.model.player.status.PlayerStatus
import lineage.vetal.server.login.game.model.template.CharacterTemplate
import java.util.UUID

class Player(
    id: UUID,
    name: String,
    val accountId: UUID,
    val appearance: Appearance,
    var charTemplate: CharacterTemplate,
    val level: Int = charTemplate.classBaseLevel,
    val baseClassId: Int = charTemplate.id,
    val position: SpawnLocation = SpawnLocation(charTemplate.randomSpawn)
) : Playable(id, name) {
    override var status = PlayerStatus(level, charTemplate)
    val race get() = charTemplate.classId.race.ordinal
    val classId get() = charTemplate.classId.ordinal

    var karma: Int = 0; private set
    var pvpKills: Int = 0; private set
    var pkKills: Int = 0; private set
    var clanId: Int = 0; private set
    var deleteTimer = 0L; private set
    var hasDwarvenCraft = false; private set
    var title: String? = "THE FUCK"; private set
    var accessLevel = 0; private set
    var isOnlineInt = 0; private set
    var isIn7sDungeon = false; private set
    var clanPrivileges = 0; private set
    var wantsPeace = false; private set
    var isNoble = false; private set
}