package lineage.vetal.server.game.game.manager.behaviour

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.gameserver.packet.server.Attack

private const val TAG = "BehaviourAttackUseCase"

object BehaviourAttackUseCase {
    fun onBehaviourAttack(context: GameContext, creature: CreatureObject, currentIntention: Intention.ATTACK): Boolean {
        val attackData = currentIntention.data
        val time = context.clock.millis()

        if (time < attackData.nextTime) {
            System.err.println("ATTACK to early")
            return false
        }

        val target = currentIntention.data.target
        writeDebug(TAG, "${creature.name} attacks ${target.name}")
        val hit = Attack.Hit(target, 50, miss = false, crit = false, shield = false, useSS = false, ssGrade = 0)
        context.gameWorld.broadCast(creature.region, Attack(creature, listOf(hit)))
        attackData.lastTime = time
        attackData.nextTime = time + 1000
        return false
    }
}