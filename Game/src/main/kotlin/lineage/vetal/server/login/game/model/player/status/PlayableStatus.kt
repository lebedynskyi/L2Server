package lineage.vetal.server.login.game.model.player.status

import lineage.vetal.server.login.game.model.template.pc.CharTemplate

abstract class PlayableStatus(
    private val template: CharTemplate
) : CreatureStatus(template) {
    override fun getINT(): Int {
        return template.baseINT
    }

    override fun getSTR(): Int {
        return template.baseSTR
    }

    override fun getCON(): Int {
        return template.baseCON
    }

    override fun getMEN(): Int {
        return template.baseMEN
    }

    override fun getDEX(): Int {
        return template.baseDEX
    }

    override fun getWIT(): Int {
        return template.baseWIT
    }
}