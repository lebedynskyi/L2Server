package lineage.vetal.server.game.game.manager.behaviour

import lineage.vetal.server.game.game.model.behaviour.BehaviourResult
import lineage.vetal.server.game.game.model.player.CreatureObject

abstract class BehaviourTask(val creature: CreatureObject) {
    abstract suspend fun step(): BehaviourResult
    abstract fun nextDelay(): Long
}
