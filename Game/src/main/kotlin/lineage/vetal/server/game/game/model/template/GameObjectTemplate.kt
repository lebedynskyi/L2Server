package lineage.vetal.server.game.game.model.template

import lineage.vetal.server.game.xml.StatSet

abstract class GameObjectTemplate(
    set: StatSet,
    val id: Int = set.getInteger("id")
)