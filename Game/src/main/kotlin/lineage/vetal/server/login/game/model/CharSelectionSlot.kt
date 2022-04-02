package lineage.vetal.server.login.game.model

import lineage.vetal.server.login.game.model.player.Sex

data class CharSelectionSlot(
    val objectId: Int,
    val name: String,
    val title: String,
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
    var sex: Sex,
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


    private val _paperdoll: Array<IntArray> = emptyArray()

    fun getPaperdollObjectId(slot: Int): Int {
        return _paperdoll[slot][0]
    }

    fun getPaperdollItemId(slot: Int): Int {
        return _paperdoll[slot][1]
    }

//    val enchantEffect: Int
//        get() = _paperdoll[Inventory.PAPERDOLL_RHAND][2]

}