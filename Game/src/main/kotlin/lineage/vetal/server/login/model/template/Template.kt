package lineage.vetal.server.login.model.template

import lineage.vetal.server.login.xml.StatSet

open class Template(
    set: StatSet
) {
    val id = set.getInteger("id")
}