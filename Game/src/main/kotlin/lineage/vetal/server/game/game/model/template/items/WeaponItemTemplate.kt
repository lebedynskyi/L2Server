package lineage.vetal.server.game.game.model.template.items

import lineage.vetal.server.game.xml.StatSet

enum class WeaponType(
    val range: Int
) {
    NONE(40), SWORD(40), BLUNT(40), DAGGER(40), BOW(500), POLE(66),
    ETC(40), FIST(40), DUAL(40), DUALFIST(40), BIGSWORD(40),
    FISHINGROD(40), BIGBLUNT(40), PET(40)
}

class WeaponItemTemplate(
    set: StatSet
) : ItemTemplate(set) {
    override val type1 = TYPE1_WEAPON_RING_EARRING_NECKLACE
    override val type2 = TYPE2_WEAPON

    val weaponType = set.getEnum("weapon_type", WeaponType::class.java, WeaponType.NONE);
}