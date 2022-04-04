package lineage.vetal.server.login.game.model.template

import lineage.vetal.server.core.model.StatSet

class ItemTemplate(set: StatSet) : Template(set) {
    val isEquipped = set.getBool("isEquipped", true)
    val count = set.getInteger("count")
}