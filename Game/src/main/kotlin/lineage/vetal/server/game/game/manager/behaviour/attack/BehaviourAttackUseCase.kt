package lineage.vetal.server.game.game.manager.behaviour.attack

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.formulas.PAttackFormula
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.gameserver.packet.server.Attack

private const val TAG = "BehaviourAttackUseCase"

class BehaviourAttackUseCase {
    fun calculateHitDelay(creature: CreatureObject): Int {
        return PAttackFormula.calcHitDelay(creature, creature.stats.getPAtkSpd().toDouble())
    }

    fun isStillAttacking(creature: CreatureObject, attack: Intention.ATTACK): Boolean {
        return creature.behaviour.current === attack
    }

    fun resolveHit(context: GameContext, creature: CreatureObject, attack: Intention.ATTACK) {
        val target = attack.data.target
        val time = context.clock.millis()

        writeDebug(TAG, "${creature.name} attacks ${target.name}")
        val hit = Attack.Hit(target, 50, miss = false, crit = false, shield = false, useSS = false, ssGrade = 0)
        context.broadcaster.broadCast(creature.region, Attack(creature, listOf(hit)))
        attack.data.lastTime = time
    }
}
