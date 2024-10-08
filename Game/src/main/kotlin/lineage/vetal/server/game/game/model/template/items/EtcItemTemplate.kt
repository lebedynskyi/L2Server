package lineage.vetal.server.game.game.model.template.items

import lineage.vetal.server.game.xml.StatSet


enum class EtcItemType {
    NONE, ARROW, POTION, SCRL_ENCHANT_WP, SCRL_ENCHANT_AM, SCROLL, RECIPE, MATERIAL, PET_COLLAR, CASTLE_GUARD, LOTTO,
    RACE_TICKET, DYE, SEED, CROP, MATURECROP, HARVEST, SEED2, TICKET_OF_LORD, LURE, BLESS_SCRL_ENCHANT_WP, BLESS_SCRL_ENCHANT_AM,
    COUPON, ELIXIR, SHOT, HERB, QUEST
}

class EtcItemTemplate(
    set: StatSet
) : ItemTemplate(set) {
    override val type1: Int get() = TYPE1_ITEM_QUEST_ITEM_ADENA;
    override val type2: Int
        get() = when {
            etcType == EtcItemType.QUEST -> TYPE2_QUEST
            id == 57 || id == 5575 -> TYPE2_MONEY
            else -> TYPE2_OTHER
        }

    val isQuest: Boolean get() = etcType == EtcItemType.QUEST && type2 != TYPE2_MONEY
    val etcType = set.getEnum("etcitem_type", EtcItemType::class.java, EtcItemType.NONE);
    val itemHandlerName = set.getString("handler", null)
}