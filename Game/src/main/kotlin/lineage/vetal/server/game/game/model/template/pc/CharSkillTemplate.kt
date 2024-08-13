package lineage.vetal.server.game.game.model.template.pc

import lineage.vetal.server.game.game.model.template.GameObjectTemplate
import lineage.vetal.server.game.xml.StatSet

class CharSkillTemplate(set: StatSet) : GameObjectTemplate(set) {
    val minLvl = set.getInteger("minLvl")
    val lvl = set.getInteger("lvl")
    val cost = set.getInteger("cost", -1)
}