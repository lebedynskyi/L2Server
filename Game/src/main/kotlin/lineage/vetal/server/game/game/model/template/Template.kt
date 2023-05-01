package lineage.vetal.server.game.game.model.template

import lineage.vetal.server.game.xml.StatSet

open class Template(
    set: StatSet,
    val id: Int = set.getInteger("id")
)