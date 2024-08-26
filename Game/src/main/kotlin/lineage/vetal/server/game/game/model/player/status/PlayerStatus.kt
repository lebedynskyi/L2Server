package lineage.vetal.server.game.game.model.player.status

import lineage.vetal.server.game.game.model.template.pc.CharTemplate

class PlayerStatus(
    private val template: CharTemplate
) : PlayableStatus(template) {
    var level: Int = 1

    var curCp: Double = 0.0
    var exp: Long = 0
    var sp: Int = 0

    var isSitting = false
    var isFishing = false
    var isMounted = false
    var mountType: Int = 0
    var mountNpcId: Int = 0

    fun getBaseSwimSpeed() = template.baseSwimSpeed

    fun getMaxCp(): Double {
        return template.getBaseCpMax(level)
    }

    override fun getMaxHp(): Double {
        return template.getBaseHpMax(level)
    }

    override fun getMaxMp(): Double {
        return template.getBaseMpMax(level)
    }
}