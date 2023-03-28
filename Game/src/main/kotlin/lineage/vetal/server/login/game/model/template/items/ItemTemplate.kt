package lineage.vetal.server.login.game.model.template.items

import lineage.vetal.server.login.game.model.template.Template
import lineage.vetal.server.login.xml.StatSet

class ItemTemplate(set: StatSet) : Template(set) {
    val isEquipped = set.getBool("isEquipped", true)
    val count = set.getInteger("count")
}