package lineage.vetal.server.login.game.model.template

import lineage.vetal.server.login.xml.StatSet

open class Template(
    set: StatSet,
    val id: Int = set.getInteger("id")
)