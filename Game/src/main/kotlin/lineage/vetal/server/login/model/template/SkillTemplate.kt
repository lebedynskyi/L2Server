package lineage.vetal.server.login.model.template

import lineage.vetal.server.login.model.StatsSet

class SkillTemplate(set: StatsSet) : Template(set) {
    val minLvl = set.getInteger("minLvl")
    val lvl = set.getInteger("lvl")
}