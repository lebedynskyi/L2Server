package lineage.vetal.server.game.game.model

import lineage.vetal.server.game.game.model.inventory.WearableInventory
import lineage.vetal.server.game.game.model.player.CharacterSex

data class CharSelectionSlot(
    val id: String,
    val objectId: Int,
    val name: String,
    val title: String?,
    var charId: Int = 0x00030b7a,
    var exp: Long,
    var sp: Int,
    var clanId: Int,
    var race: Int,
    var classId: Int,
    var baseClassId: Int,
    var deleteTimer: Long,
    var lastAccess: Long,
    var face: Int,
    var hairStyle: Int,
    var hairColor: Int,
    var sex: CharacterSex,
    var level: Int,
    var maxHp: Double,
    var currentHp: Double,
    var maxMp: Double,
    var currentMp: Double,
    var karma: Int,
    var pkKills: Int,
    var pvPKills: Int,
    var augmentationId: Int,
    var x: Int,
    var y: Int,
    var z: Int,
    var accessLevel: Int
) {
    lateinit var inventory: WearableInventory
}