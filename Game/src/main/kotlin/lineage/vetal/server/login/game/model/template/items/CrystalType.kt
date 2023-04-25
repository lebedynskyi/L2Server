package lineage.vetal.server.login.game.model.template.items

enum class CrystalType(
    private val id: Int = 0,
    private val crystalId: Int = 0,
    private val crystalEnchantBonusArmor: Int = 0,
    private val crystalEnchantBonusWeapon: Int = 0
) {
    NONE(1, 2, 3, 4),
    D(1, 2, 3, 4),
    C(1, 2, 3, 4),
    B(1, 2, 3, 4),
    A(1, 2, 3, 4),
    S(1, 2, 3, 4)
}