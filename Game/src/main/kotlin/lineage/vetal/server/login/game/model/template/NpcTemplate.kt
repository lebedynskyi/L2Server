package lineage.vetal.server.login.game.model.template

import lineage.vetal.server.login.xml.StatSet

open class NpcTemplate(
    set: StatSet
) : CreatureTemplate(set) {
    private val npcId = set.getInteger("id")
    private val idTemplate = set.getInteger("idTemplate", npcId)
    private val type = set.getString("type")
    private val name = set.getString("name")
    private val usingServerSideName = set.getBool("usingServerSideName", false)
    private val title = set.getString("title", "")
    private val usingServerSideTitle = set.getBool("usingServerSideTitle", false)
    private val level = set.getByte("level", 1.toByte())
    private val exp = set.getInteger("exp", 0)
    private val sp = set.getInteger("sp", 0)
    private val rHand = set.getInteger("rHand", 0)
    private val lHand = set.getInteger("lHand", 0)
    private val corpseTime = set.getInteger("corpseTime", 7)
    private val enchantEffect = set.getInteger("enchant", 0)
    private val dropHerbGroup = set.getInteger("dropHerbGroup", 0)
    private val race: NpcRace = NpcRace.values()[set.getInteger("raceId", 0)]
    private val ssCount = set.getInteger("ssCount", 0)
    private val ssRate = set.getInteger("ssRate", 0)
    private val spsCount = set.getInteger("spsCount", 0)
    private val spsRate = set.getInteger("spsRate", 0)
    private val aggroRange = set.getInteger("aggro", 0)

    private val canMove = set.getBool("canMove", true)
    private val isSeedable = set.getBool("seedable", false)

    private lateinit var ignoredIds: IntArray
    private lateinit var clans: Array<String>
    private val clanRange = 0


//    private val aiType: NpcAiType? = null
//    private val categories: List<DropCategory>? = null
//    private val minions: List<MinionData>? = null
//    private val teachInfo: List<ClassId>? = null

//    private val skills: Map<NpcSkillType, List<L2Skill>> = HashMap<NpcSkillType, List<L2Skill>>()
//    private val questEvents: Map<ScriptEventType, List<Quest>> = HashMap<ScriptEventType, List<Quest>>()

//    private val castle: Castle? = null
//    private val clanHall: ClanHall? = null
//    private val siegableHall: SiegableHall? = null

    init {
//        aiType = set.getEnum("aiType", NpcAiType::class.java, NpcAiType.DEFAULT)
//
//        if (set.containsKey("clan")) {
//            clans = set.getStringArray("clan")
//            clanRange = set.getInteger("clanRange")
//            if (set.containsKey("ignoredIds")) ignoredIds = set.getIntegerArray("ignoredIds")
//        }
//
//        categories = set.getList("drops")
//        minions = set.getList("minions")
//
//        if (set.containsKey("teachTo")) {
//            val classIds = set.getIntegerArray("teachTo")
//            teachInfo = ArrayList<ClassId>(classIds.size)
//            for (classId in classIds) teachInfo.add(ClassId.VALUES.get(classId))
//        }
//
//        addSkills(set.getList("skills"))
//
//        // Set the Castle if existing.
//
//        // Set the Castle if existing.
//        for (castle in CastleManager.getInstance().getCastles()) {
//            if (castle.getRelatedNpcIds().contains(npcId)) {
//                castle = castle
//                break
//            }
//        }
//
//        // Set the ClanHall if existing.
//
//        // Set the ClanHall if existing.
//        for (ch in ClanHallManager.getInstance().getClanHalls().values()) {
//            if (ArraysUtil.contains(ch.getRelatedNpcIds(), npcId)) {
//                if (ch is SiegableHall) siegableHall = ch as SiegableHall
//                clanHall = ch
//                break
//            }
//        }
    }
}