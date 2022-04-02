package lineage.vetal.server.login.game.model.template

import lineage.vetal.server.login.xml.StatSet

class ItemTemplate(set: StatSet) : Template(set) {
    val isEquipped = set.getBool("isEquipped", true)
    val count = set.getInteger("count")
}