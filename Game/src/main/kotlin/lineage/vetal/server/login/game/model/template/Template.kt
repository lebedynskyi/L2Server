package lineage.vetal.server.login.game.model.template

import lineage.vetal.server.core.model.StatSet

open class Template(
    set: StatSet
) {
    val id = set.getInteger("id")
}