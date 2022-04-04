package lineage.vetal.server.login.game.model.player.status

import lineage.vetal.server.login.game.model.template.CharacterTemplate

class PlayerStatus(
    private val template: CharacterTemplate
) : PlayableStatus(template) {
    var level: Int = 0
    var maxHp: Int = 0
    var hp: Double = 0.0
    var maxCp: Int = 0
    var cp: Double = 0.0
    var maxMp: Int = 0
    var mp: Double = 0.0
    var exp: Long = 0
    var sp: Int = 0

    override fun getBaseSwimSpeed(): Int {
        return template.baseSwimSpeed
    }
}