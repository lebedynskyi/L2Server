package lineage.vetal.server.login.model.template

import lineage.vetal.server.login.model.StatsSet

class ItemTemplate(set: StatsSet) : Template(set) {
    val isEquipped = set.getBool("isEquipped", true)
    val count = set.getInteger("count")
}