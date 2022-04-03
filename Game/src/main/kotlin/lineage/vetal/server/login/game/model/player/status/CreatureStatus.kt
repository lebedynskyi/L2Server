package lineage.vetal.server.login.game.model.player.status

import lineage.vetal.server.login.game.model.template.CreatureTemplate

open class CreatureStatus(
    private val template: CreatureTemplate
) {
    open fun getINT(): Int {
        return template.baseINT
    }

    open fun getSTR(): Int {
        return template.baseSTR
    }

    open fun getCON(): Int {
        return template.baseCON
    }

    open fun getMEN(): Int {
        return template.baseMEN
    }

    open fun getDEX(): Int {
        return template.baseDEX
    }

    open fun getWIT(): Int {
        return template.baseWIT
    }
}