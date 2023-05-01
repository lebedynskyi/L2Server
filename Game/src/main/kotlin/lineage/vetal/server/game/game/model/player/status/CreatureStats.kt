package lineage.vetal.server.game.game.model.player.status

import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.template.pc.CreatureTemplate

abstract class CreatureStats(
    private val template: CreatureTemplate
) {
    var isRunning = true

    open fun getINT(): Int {
        return template.baseINT
    }

    open fun getSTR(): Int {
        return template.baseSTR
    }

    open fun getCON(): Int {
        return template.baseCON
    }

    open fun getMEN(): Int {
        return template.baseMEN
    }

    open fun getDEX(): Int {
        return template.baseDEX
    }

    open fun getWIT(): Int {
        return template.baseWIT
    }

    open fun getPAtk(target: CreatureObject?): Int {
        return template.basePAtk.toInt()
    }

    open fun getPDef(target: PlayerObject?): Int {
        return template.basePDef.toInt()
    }

    open fun getCriticalHit(target: PlayerObject?, skill: Any?): Int {
        return template.baseCritRate
    }

    open fun getMAtk(target: PlayerObject?, skill: Any?): Int {
        return template.baseMAtk.toInt()
    }

    open fun getMDef(target: PlayerObject?, skill: Any?): Int {
        return template.baseMDef.toInt()
    }

    open fun getPAtkSpd(): Int {
        return template.basePAtkSpd
    }

    open fun getBaseRunSpeed(): Int {
        return template.baseRunSpeed
    }

    open fun getBaseWalkSpeed(): Int {
        return template.baseWalkSpeed
    }

    abstract fun getBaseSwimSpeed(): Int

    fun getMovementSpeedMultiplier(): Double {
        return ((getMoveSpeed() / getBaseMoveSpeed()).toDouble()) * 4
    }

    fun getAttackSpeedMultiplier(): Double {
        return 1.1 * getPAtkSpd() / template.basePAtkSpd
    }

    // -------------     Calculation zone    --------------

    open fun getMAtkSpd(): Int {
        return 42
    }

    open fun getEvasionRate(target: PlayerObject?): Int {
        return 42
    }

    open fun getAccuracy(): Int {
        return 42
    }

    open fun getMoveSpeed(): Float {
        return 42F
    }

    // -------------   Protected  Calculation zone    --------------

    protected fun getBaseMoveSpeed(): Int {
        return if (isRunning) getBaseRunSpeed() else getBaseWalkSpeed()
    }
}