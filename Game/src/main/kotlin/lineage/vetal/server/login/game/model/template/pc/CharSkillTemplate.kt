package lineage.vetal.server.login.game.model.template.pc

import lineage.vetal.server.login.game.model.template.Template
import lineage.vetal.server.login.xml.StatSet

class CharSkillTemplate(set: StatSet) : Template(set) {
    val minLvl = set.getInteger("minLvl")
    val lvl = set.getInteger("lvl")
    val cost = set.getInteger("cost", -1)
}