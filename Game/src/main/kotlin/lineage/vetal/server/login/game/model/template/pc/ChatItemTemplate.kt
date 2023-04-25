package lineage.vetal.server.login.game.model.template.pc

import lineage.vetal.server.login.game.model.template.Template
import lineage.vetal.server.login.xml.StatSet

class ChatItemTemplate(set: StatSet) : Template(set) {
    val count = set.getInteger("count")
}