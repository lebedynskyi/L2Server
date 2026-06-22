package lineage.vetal.server.game.game.manager.behaviour.attack

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.behaviour.data.AttackData
import lineage.vetal.server.game.game.model.intenttion.Intention
import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.task.ScheduleTaskManager
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap

class AttackManager(
    private val context: GameContext,
    private val attackUseCase: BehaviourAttackUseCase = BehaviourAttackUseCase(),
    clock: Clock,
    dispatcher: CoroutineDispatcher,
) : ScheduleTaskManager(clock, dispatcher) {
    private val activeJobs = ConcurrentHashMap<Int, Job>()

    fun onCreatureAttack(player: PlayerObject, actionTarget: CreatureObject) {
        cancelAttack(player)

        val attackData = AttackData(actionTarget)
        val attack = Intention.ATTACK(attackData)
        player.behaviour.setIntention(attack)

        val delay = attackUseCase.calculateHitDelay(player).toLong()
        scheduleHit(player, attack, delay)
    }

    // Other managers (e.g. movement) call this before swapping intention away from ATTACK.
    fun cancelAttack(creature: CreatureObject) {
        activeJobs.remove(creature.objectId)?.cancel()
    }

    internal fun scheduleHit(creature: CreatureObject, attack: Intention.ATTACK, delay: Long) {
        val hit = AttackHitTask(context, attackUseCase, creature, attack, this)
        activeJobs[creature.objectId] = schedule(hit, delay)
    }
}
