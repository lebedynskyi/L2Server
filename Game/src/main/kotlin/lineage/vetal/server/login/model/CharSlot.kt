package lineage.vetal.server.login.model


/**
 * A datatype used to store character selection screen informations.
 */
data class CharSlot(
    val objectId: Int,
    val name: String
) {
    var charId = 0x00030b7a
    var exp: Long = 0
    var sp = 0
    var clanId = 0
    var race = 0
    var classId = 0
    var baseClassId = 0
    var deleteTimer = 0L
    var lastAccess = 0L
    var face = 0
    var hairStyle = 0
    var hairColor = 0
    var sex = 0
    var level = 1
    var maxHp = 0
    var currentHp = 0.0
    var maxMp = 0
    var currentMp = 0.0
    var karma = 0
    var pkKills = 0
    var pvPKills = 0
    var augmentationId = 0
    var x = 0
    var y = 0
    var z = 0
    var accessLevel = 0

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