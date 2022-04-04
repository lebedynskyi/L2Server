package lineage.vetal.server.login.game.model.template

import lineage.vetal.server.core.model.location.Location
import lineage.vetal.server.login.game.model.player.ClassId
import lineage.vetal.server.login.game.model.player.Sex
import lineage.vetal.server.core.model.StatSet

class CharacterTemplate(set: StatSet) : CreatureTemplate(set) {
    val charClass: ClassId = ClassId.VALUES[id]
    val fallHeight: Int = set.getInteger("falling_height", 333)
    val baseSwimSpeed: Int = set.getInteger("swimSpd", 1)
    val classBaseLevel: Int = set.getInteger("baseLvl")
    val fists: Int = set.getInteger("fists")

    val items: List<ItemTemplate> get() = _items
    val skills: List<SkillTemplate> get() = _skills

    private val _items: MutableList<ItemTemplate> = set.getList<ItemTemplate>("items").toMutableList()
    private val _skills: MutableList<SkillTemplate> = set.getList<SkillTemplate>("skills").toMutableList()
    private val _collisionRadiusFemale: Double = set.getDouble("radiusFemale")
    private val _collisionHeightFemale: Double = set.getDouble("heightFemale")
    private val _spawnLocations: List<Location> = set.getList("spawnLocations")
    private val _hpTable: DoubleArray = set.getDoubleArray("hpTable")
    private val _mpTable: DoubleArray = set.getDoubleArray("mpTable")
    private val _cpTable: DoubleArray = set.getDoubleArray("cpTable")

    val randomSpawn: Location get() = _spawnLocations.random()

    fun getCollisionRadiusBySex(sex: Sex): Double {
        return if (sex === Sex.MALE) collisionRadius else _collisionRadiusFemale
    }

    fun getCollisionHeightBySex(sex: Sex): Double {
        return if (sex === Sex.MALE) collisionHeight else _collisionHeightFemale
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

    fun findSkill(id: Int, level: Int): SkillTemplate? {
        return skills.asSequence()
            .filter { s: SkillTemplate -> s.id == id && s.lvl == level }
            .firstOrNull()
    }

    fun addSkillsFromParent(parentSkills: List<SkillTemplate>) {
        _skills.addAll(skills)
    }

    override fun toString(): String {
        return charClass.className
    }
}