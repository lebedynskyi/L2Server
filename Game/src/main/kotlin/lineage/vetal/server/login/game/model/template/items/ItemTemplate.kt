package lineage.vetal.server.login.game.model.template.items

import lineage.vetal.server.login.game.model.template.Template
import lineage.vetal.server.login.xml.StatSet

class ItemTemplate(set: StatSet) : Template(set) {
    val name = set.getString("name")
    val weight = set.getInteger("weight", 0)
    val materialType = set.getEnum("material", MaterialType::class.java, MaterialType.STEEL)
    val duration = set.getInteger("duration", -1)
    val bodyPart = CharacterSlot.Slots[set.getString("bodypart", "none")]
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
    val defaultAction = set.getEnum("default_action", ItemAction:: class.java, ItemAction.none)
    val skills = if (set.containsKey("item_skill")) set.getIntPairsArray("item_skill") else null
    val itemType: ItemType = set.getEnum("type", ItemType::class.java, ItemType.EtcItem)

    var type1 = 0 // needed for item list (inventory)
    var type2 = 0 // different lists for armor, weapon, etc
}