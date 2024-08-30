package lineage.vetal.server.game.game.formulas

import lineage.vetal.server.game.game.model.player.CreatureObject
import kotlin.math.max

object PAttackFormula {
    fun calcAttackDelay(attacker: CreatureObject, atkSpd: Double, reuse: Double = 500000.0): Int {
        // Originaly some config. Maybe for x100000 servers with fast attack spd
//        if (attacker is L2PcInstance) {
//            base *= Config.ALT_ATTACK_DELAY
//        }

        val clearedAttackSpd = max(atkSpd, 10.0)

        return (reuse / clearedAttackSpd).toInt()
    }
}