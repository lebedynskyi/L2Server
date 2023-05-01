package lineage.vetal.server.game.game.model.template.pc

import lineage.vetal.server.game.game.model.template.Template
import lineage.vetal.server.game.xml.StatSet

class ChatItemTemplate(set: StatSet) : Template(set) {
    val count = set.getInteger("count")
}