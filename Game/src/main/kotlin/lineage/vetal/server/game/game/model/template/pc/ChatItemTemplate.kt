package lineage.vetal.server.game.game.model.template.pc

import lineage.vetal.server.game.game.model.template.GameObjectTemplate
import lineage.vetal.server.game.xml.StatSet

class ChatItemTemplate(set: StatSet) : GameObjectTemplate(set) {
    val count = set.getInteger("count")
}