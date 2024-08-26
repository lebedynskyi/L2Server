package lineage.vetal.server.game.game.model.behaviour.data

import lineage.vetal.server.game.game.model.player.CreatureObject

data class TargetData(
    val target: CreatureObject,
    var lastTime: Long = 0,
)