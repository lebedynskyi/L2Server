package lineage.vetal.server.game.game.model.player.status

import lineage.vetal.server.game.game.model.template.pc.CharTemplate

class PlayerStatus(
    private val template: CharTemplate
) : PlayableStatus(template) {
    var level: Int = 1
    var maxHp: Int = 0
    var curHp: Double = 0.0

    var maxCp: Int = 0
    var curCp: Double = 0.0

    var maxMp: Int = 0
    var curMp: Double = 0.0

    var exp: Long = 0
    var sp: Int = 0

    var isSitting = false
    var isFishing = false
    var isMounted = false
    var mountType: Int = 0
    var mountNpcId: Int = 0

    fun getBaseSwimSpeed() = template.baseSwimSpeed
}