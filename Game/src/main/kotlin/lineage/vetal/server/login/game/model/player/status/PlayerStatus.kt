package lineage.vetal.server.login.game.model.player.status

import lineage.vetal.server.login.game.model.template.pc.CharTemplate

class PlayerStatus(
    private val template: CharTemplate
) : PlayableStatus(template) {
    var level: Int = 0
    var maxHp: Int = 0
    var curHp: Double = 0.0
    var maxCp: Int = 0
    var curCp: Double = 0.0
    var maxMp: Int = 0
    var curMp: Double = 0.0
    var exp: Long = 0
    var sp: Int = 0

    override fun getBaseSwimSpeed(): Int {
        return template.baseSwimSpeed
    }
}