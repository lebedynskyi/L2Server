package lineage.vetal.server.login.model.template

import lineage.vetal.server.login.xml.StatSet

class SkillTemplate(set: StatSet) : Template(set) {
    val minLvl = set.getInteger("minLvl")
    val lvl = set.getInteger("lvl")
    val cost = set.getInteger("cost", -1)
}