package lineage.vetal.server.game.game.model.player.status

import lineage.vetal.server.game.game.model.template.pc.CharTemplate

class PlayerStatus(
    private val template: CharTemplate
) : PlayableStatus(template) {
    var sp: Int = 0
    var curCp: Double = getMaxCp()

    var isSitting = false
    var isFishing = false
    var isMounted = false
    var mountType: Int = 0
    var mountNpcId: Int = 0

    fun getBaseSwimSpeed() = template.baseSwimSpeed

    fun getMaxCp(): Double {
        return template.getBaseCpMax(level)
    }
}