package lineage.vetal.server.game.game.model.player.status

import lineage.vetal.server.game.game.model.player.CreatureObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.template.pc.CreatureTemplate

abstract class CreatureStats(
    private val template: CreatureTemplate
) {
    var isRunning = true
    var curHp: Double = template.baseHpMax
    var curMp: Double = template.baseMpMax

    // Stats
    open fun getINT() = template.baseINT
    open fun getSTR() = template.baseSTR
    open fun getCON() = template.baseCON
    open fun getMEN() = template.baseMEN
    open fun getDEX() = template.baseDEX
    open fun getWIT() = template.baseWIT

    // Multipliers are used by game client ot display animation
    fun getMovementSpeedMultiplier() = getMoveSpeed() / getBaseMoveSpeed().toDouble()
    fun getAttackSpeedMultiplier() = 1.1 * getPAtkSpd() / template.basePAtkSpd

    open fun getBaseRunSpeed(): Int = template.baseRunSpeed
    open fun getBaseWalkSpeed(): Int = template.baseWalkSpeed
    open fun getBaseMoveSpeed(): Int = if (isRunning) getBaseRunSpeed() else getBaseWalkSpeed()

    // TODO list of hennas, Buffs, passive skills

    // -------------     Calculation zone    --------------
    open fun getMaxHp(): Double {
        return template.baseHpMax
    }

    open fun getMaxMp(): Double {
        return template.baseMpMax
    }

    open fun getMoveSpeed(): Double {
        return getBaseMoveSpeed().toDouble()
    }

    open fun getPAtk(target: CreatureObject?): Int {
        return template.basePAtk.toInt()
    }

    open fun getPAtkSpd(): Int {
        return template.basePAtkSpd
    }

    open fun getCriticalHit(target: PlayerObject?, skill: Any?): Int {
        return template.baseCritRate
    }

    open fun getMAtk(target: PlayerObject?, skill: Any?): Int {
        return template.baseMAtk.toInt()
    }

    open fun getPDef(target: PlayerObject?): Int {
        return template.basePDef.toInt()
    }

    open fun getMDef(target: PlayerObject?, skill: Any?): Int {
        return template.baseMDef.toInt()
    }

    open fun getMAtkSpd(): Int {
        return 42
    }

    open fun getEvasionRate(target: PlayerObject?): Int {
        return 42
    }

    open fun getAccuracy(): Int {
        return 42
    }
}