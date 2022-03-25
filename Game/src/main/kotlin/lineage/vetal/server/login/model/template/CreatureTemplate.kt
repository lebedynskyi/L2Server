package lineage.vetal.server.login.model.template

open class CreatureTemplate {
    val baseSTR = 40
    val baseCON = 21
    val baseDEX = 30
    val baseINT = 20
    val baseWIT = 43
    val baseMEN = 20

    val baseHpMax = 0.0
    val baseMpMax = 0.0

    val baseHpReg = 1.5
    val baseMpReg = 0.9

    //TODO
    val basePAtk = 0.0
    val baseMAtk = 0.0
    val basePDef = 0.0
    val baseMDef = 0.0

    val basePAtkSpd = 300

    val baseCritRate = 4

    val baseWalkSpd = 0
    val baseRunSpd = 1

    // TODO
    protected val collisionRadius = 0.0
    protected val collisionHeight = 0.0
}