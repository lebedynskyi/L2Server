package lineage.vetal.server.game.game.model.template.items

import lineage.vetal.server.game.game.model.template.GameObjectTemplate
import lineage.vetal.server.game.xml.StatSet

const val TYPE1_WEAPON_RING_EARRING_NECKLACE = 0
const val TYPE1_SHIELD_ARMOR = 1
const val TYPE1_ITEM_QUEST_ITEM_ADENA = 4

const val TYPE2_WEAPON = 0
const val TYPE2_SHIELD_ARMOR = 1
const val TYPE2_ACCESSORY = 2
const val TYPE2_QUEST = 3
const val TYPE2_MONEY = 4
const val TYPE2_OTHER = 5

enum class ItemAction {
    calc, call_skill, capsule, create_mpcc, dice, equip, fishingshot, harvest, hide_name,
    keep_exp, nick_color, none, peel, recipe, seed, show_adventurer_guide_book, show_html, show_ssq_status,
    skill_maintain, skill_reduce, soulshot, spiritshot, start_quest, summon_soulshot, summon_spiritshot, xmas_open
}

enum class MaterialType {
    STEEL, FINE_STEEL, COTTON, BLOOD_STEEL, BRONZE, SILVER, GOLD, MITHRIL,
    ORIHARUKON, PAPER, WOOD, CLOTH, LEATHER, BONE, HORN, DAMASCUS, ADAMANTAITE, CHRYSOLITE,
    CRYSTAL, LIQUID, SCALE_OF_DRAGON, DYESTUFF, COBWEB;
}

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

abstract class ItemTemplate(set: StatSet) : GameObjectTemplate(set) {
    abstract val type1: Int // needed for item list (inventory)
    abstract val type2: Int // different lists for armor, weapon, etc

    val name = set.getString("name")
    val weight = set.getInteger("weight", 0)
    val materialType = set.getEnum("material", MaterialType::class.java, MaterialType.STEEL)
    val duration = set.getInteger("duration", -1)
    val bodySlot: ItemSlot = ItemSlot.fromValue(set.getString("bodypart", null))
    val referencePrice = set.getInteger("price", 0)
    val crystalType = set.getEnum("crystal_type", CrystalType::class.java, CrystalType.NONE)
    val crystalCount = set.getInteger("crystal_count", 0)
    val stackable = set.getBool("is_stackable", false)
    val sellable = set.getBool("is_sellable", true)
    val dropable = set.getBool("is_dropable", true)
    val destroyable = set.getBool("is_destroyable", true)
    val tradable = set.getBool("is_tradable", true)
    val depositable = set.getBool("is_depositable", true)
    val isHeroItem = (id in 6611..6621) || id == 6842
    val isOlyRestricted = set.getBool("is_oly_restricted", false)
    val defaultAction = set.getEnum("default_action", ItemAction::class.java, ItemAction.none)
    val skills = if (set.containsKey("item_skill")) set.getIntPairsArray("item_skill") else null
}