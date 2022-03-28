package lineage.vetal.server.login.model.template

import lineage.vetal.server.login.xml.StatSet

open class CreatureTemplate(set: StatSet) : Template(set){
    val baseSTR = set.getInteger("str", 40)
    val baseCON = set.getInteger("con", 21)
    val baseDEX = set.getInteger("dex", 30)
    val baseINT = set.getInteger("int", 20)
    val baseWIT = set.getInteger("wit", 43)
    val baseMEN = set.getInteger("men", 20)
    val baseHpReg = set.getDouble("hpRegen", 1.5)
    val baseMpReg = set.getDouble("mpRegen", 0.9)
    val basePAtk = set.getDouble("pAtk")
    val baseMAtk = set.getDouble("mAtk")
    val basePDef = set.getDouble("pDef")
    val baseMDef = set.getDouble("mDef")
    val basePAtkSpd = set.getInteger("atkSpd", 300)
    val baseCritRate = set.getInteger("crit", 4)
    val baseWalkSpeed = set.getInteger("walkSpd", 0)
    val baseRunSpeed = set.getInteger("runSpd", 1)
    val collisionRadius = set.getDouble("radius")
    val collisionHeight = set.getDouble("height")

    private val _baseMpMax = set.getDouble("mp", 0.0)
    private val _baseHpMax = set.getDouble("hp", 0.0)

    open fun getBaseHpMax(level: Int): Double {
        return _baseHpMax
    }

    open fun getBaseMpMax(level: Int): Double {
        return _baseMpMax
    }
}