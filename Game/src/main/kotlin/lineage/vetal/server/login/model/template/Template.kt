package lineage.vetal.server.login.model.template

import lineage.vetal.server.login.model.StatsSet

open class Template(
    set: StatsSet
) {
    val id = set.getInteger("id")
}