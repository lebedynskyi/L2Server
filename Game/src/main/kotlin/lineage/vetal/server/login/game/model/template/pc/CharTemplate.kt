package lineage.vetal.server.login.game.model.template.pc

import lineage.vetal.server.login.game.model.position.Position
import lineage.vetal.server.login.game.model.player.ClassId
import lineage.vetal.server.login.game.model.player.CharacterSex
import lineage.vetal.server.login.xml.StatSet

class CharTemplate(set: StatSet) : CreatureTemplate(set) {
    val charClass: ClassId = ClassId.VALUES[id]
    val fallHeight: Int = set.getInteger("falling_height", 333)
    val baseSwimSpeed: Int = set.getInteger("swimSpd", 1)
    val classBaseLevel: Int = set.getInteger("baseLvl")
    val fists: Int = set.getInteger("fists")
    val startItems: List<ChatItemTemplate> get() = _items
    val skills: List<CharSkillTemplate> get() = _skills
    val spawnLocations: List<Position> = set.getList("spawnLocations")

    private val _items: MutableList<ChatItemTemplate> = set.getList<ChatItemTemplate>("items").toMutableList()
    private val _skills: MutableList<CharSkillTemplate> = set.getList<CharSkillTemplate>("skills").toMutableList()
    private val _collisionRadiusFemale: Double = set.getDouble("radiusFemale")
    private val _collisionHeightFemale: Double = set.getDouble("heightFemale")
    private val _hpTable: DoubleArray = set.getDoubleArray("hpTable")
    private val _mpTable: DoubleArray = set.getDoubleArray("mpTable")
    private val _cpTable: DoubleArray = set.getDoubleArray("cpTable")

    fun getCollisionRadiusBySex(sex: CharacterSex): Double {
        return if (sex === CharacterSex.MALE) collisionRadius else _collisionRadiusFemale
    }

    fun getCollisionHeightBySex(sex: CharacterSex): Double {
        return if (sex === CharacterSex.MALE) collisionHeight else _collisionHeightFemale
    }

    override fun getBaseHpMax(level: Int): Double {
        return _hpTable[level - 1]
    }

    override fun getBaseMpMax(level: Int): Double {
        return _mpTable[level - 1]
    }

    fun getBaseCpMax(level: Int): Double {
        return _cpTable[level - 1]
    }

    fun findSkill(id: Int, level: Int): CharSkillTemplate? {
        return skills.asSequence()
            .filter { s: CharSkillTemplate -> s.id == id && s.lvl == level }
            .firstOrNull()
    }

    fun addSkillsFromParent(parentSkills: List<CharSkillTemplate>) {
        _skills.addAll(skills)
    }

    override fun toString(): String {
        return charClass.className
    }
}