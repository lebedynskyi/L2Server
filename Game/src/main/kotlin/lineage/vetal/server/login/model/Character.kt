package lineage.vetal.server.login.model

import java.util.*

enum class Sex {
    MALE, FEMALE, ETC;
}

enum class ClassType {
    FIGHTER, MYSTIC, PRIEST, DUMMY
}

enum class ClassRace(
    val breathMultiplier: Double
) {
    HUMAN(1.0),
    ELF(1.5),
    DARK_ELF(1.5),
    ORC(0.9),
    DWARF(0.8),
    DUMMY(0.0);

    val id : Int get() = ordinal
}

enum class ClassId(
    val race: ClassRace,
    val type: ClassType,
    val level: Int,
    val className: String,
    val parent: ClassId?
) {
    HUMAN_FIGHTER(ClassRace.HUMAN, ClassType.FIGHTER, 0, "Human Fighter", null),
    WARRIOR(ClassRace.HUMAN, ClassType.FIGHTER, 1, "Warrior", HUMAN_FIGHTER),
    GLADIATOR(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Gladiator", WARRIOR),
    WARLORD(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Warlord", WARRIOR),
    KNIGHT(ClassRace.HUMAN, ClassType.FIGHTER, 1, "Human Knight", HUMAN_FIGHTER),
    PALADIN(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Paladin", KNIGHT),
    DARK_AVENGER(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Dark Avenger", KNIGHT),
    ROGUE(ClassRace.HUMAN, ClassType.FIGHTER, 1, "Rogue", HUMAN_FIGHTER),
    TREASURE_HUNTER(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Treasure Hunter", ROGUE),
    HAWKEYE(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Hawkeye", ROGUE),
    HUMAN_MYSTIC(ClassRace.HUMAN, ClassType.MYSTIC, 0, "Human Mystic", null),
    HUMAN_WIZARD(ClassRace.HUMAN, ClassType.MYSTIC, 1, "Human Wizard", HUMAN_MYSTIC),
    SORCERER(ClassRace.HUMAN, ClassType.MYSTIC, 2, "Sorcerer", HUMAN_WIZARD),
    NECROMANCER(ClassRace.HUMAN, ClassType.MYSTIC, 2, "Necromancer", HUMAN_WIZARD),
    WARLOCK(ClassRace.HUMAN, ClassType.MYSTIC, 2, "Warlock", HUMAN_WIZARD),
    CLERIC(ClassRace.HUMAN, ClassType.PRIEST, 1, "Cleric", HUMAN_MYSTIC),
    BISHOP(ClassRace.HUMAN, ClassType.PRIEST, 2, "Bishop", CLERIC),
    PROPHET(ClassRace.HUMAN, ClassType.PRIEST, 2, "Prophet", CLERIC),
    ELVEN_FIGHTER(ClassRace.ELF, ClassType.FIGHTER, 0, "Elven Fighter", null),
    ELVEN_KNIGHT(ClassRace.ELF, ClassType.FIGHTER, 1, "Elven Knight", ELVEN_FIGHTER),
    TEMPLE_KNIGHT(ClassRace.ELF, ClassType.FIGHTER, 2, "Temple Knight", ELVEN_KNIGHT),
    SWORD_SINGER(ClassRace.ELF, ClassType.FIGHTER, 2, "Sword Singer", ELVEN_KNIGHT),
    ELVEN_SCOUT(ClassRace.ELF, ClassType.FIGHTER, 1, "Elven Scout", ELVEN_FIGHTER),
    PLAINS_WALKER(ClassRace.ELF, ClassType.FIGHTER, 2, "Plains Walker", ELVEN_SCOUT),
    SILVER_RANGER(ClassRace.ELF, ClassType.FIGHTER, 2, "Silver Ranger", ELVEN_SCOUT),
    ELVEN_MYSTIC(ClassRace.ELF, ClassType.MYSTIC, 0, "Elven Mystic", null),
    ELVEN_WIZARD(ClassRace.ELF, ClassType.MYSTIC, 1, "Elven Wizard", ELVEN_MYSTIC),
    SPELLSINGER(ClassRace.ELF, ClassType.MYSTIC, 2, "Spellsinger", ELVEN_WIZARD),
    ELEMENTAL_SUMMONER(ClassRace.ELF, ClassType.MYSTIC, 2, "Elemental Summoner", ELVEN_WIZARD),
    ELVEN_ORACLE(ClassRace.ELF, ClassType.PRIEST, 1, "Elven Oracle", ELVEN_MYSTIC),
    ELVEN_ELDER(ClassRace.ELF, ClassType.PRIEST, 2, "Elven Elder", ELVEN_ORACLE),
    DARK_FIGHTER(ClassRace.DARK_ELF, ClassType.FIGHTER, 0, "Dark Fighter", null),
    PALUS_KNIGHT(ClassRace.DARK_ELF, ClassType.FIGHTER, 1, "Palus Knight", DARK_FIGHTER),
    SHILLIEN_KNIGHT(ClassRace.DARK_ELF, ClassType.FIGHTER, 2, "Shillien Knight", PALUS_KNIGHT),
    BLADEDANCER(ClassRace.DARK_ELF, ClassType.FIGHTER, 2, "Bladedancer", PALUS_KNIGHT),
    ASSASSIN(ClassRace.DARK_ELF, ClassType.FIGHTER, 1, "Assassin", DARK_FIGHTER),
    ABYSS_WALKER(ClassRace.DARK_ELF, ClassType.FIGHTER, 2, "Abyss Walker", ASSASSIN),
    PHANTOM_RANGER(ClassRace.DARK_ELF, ClassType.FIGHTER, 2, "Phantom Ranger", ASSASSIN),
    DARK_MYSTIC(ClassRace.DARK_ELF, ClassType.MYSTIC, 0, "Dark Mystic", null),
    DARK_WIZARD(ClassRace.DARK_ELF, ClassType.MYSTIC, 1, "Dark Wizard", DARK_MYSTIC),
    SPELLHOWLER(ClassRace.DARK_ELF, ClassType.MYSTIC, 2, "Spellhowler", DARK_WIZARD),
    PHANTOM_SUMMONER(ClassRace.DARK_ELF, ClassType.MYSTIC, 2, "Phantom Summoner", DARK_WIZARD),
    SHILLIEN_ORACLE(ClassRace.DARK_ELF, ClassType.PRIEST, 1, "Shillien Oracle", DARK_MYSTIC),
    SHILLIEN_ELDER(ClassRace.DARK_ELF, ClassType.PRIEST, 2, "Shillien Elder", SHILLIEN_ORACLE),
    ORC_FIGHTER(ClassRace.ORC, ClassType.FIGHTER, 0, "Orc Fighter", null),
    ORC_RAIDER(ClassRace.ORC, ClassType.FIGHTER, 1, "Orc Raider", ORC_FIGHTER),
    DESTROYER(ClassRace.ORC, ClassType.FIGHTER, 2, "Destroyer", ORC_RAIDER),
    MONK(ClassRace.ORC, ClassType.FIGHTER, 1, "Monk", ORC_FIGHTER),
    TYRANT(ClassRace.ORC, ClassType.FIGHTER, 2, "Tyrant", MONK),
    ORC_MYSTIC(ClassRace.ORC, ClassType.MYSTIC, 0, "Orc Mystic", null),
    ORC_SHAMAN(ClassRace.ORC, ClassType.MYSTIC, 1, "Orc Shaman", ORC_MYSTIC),
    OVERLORD(ClassRace.ORC, ClassType.MYSTIC, 2, "Overlord", ORC_SHAMAN),
    WARCRYER(ClassRace.ORC, ClassType.MYSTIC, 2, "Warcryer", ORC_SHAMAN),
    DWARVEN_FIGHTER(ClassRace.DWARF, ClassType.FIGHTER, 0, "Dwarven Fighter", null),
    SCAVENGER(ClassRace.DWARF, ClassType.FIGHTER, 1, "Scavenger", DWARVEN_FIGHTER),
    BOUNTY_HUNTER(ClassRace.DWARF, ClassType.FIGHTER, 2, "Bounty Hunter", SCAVENGER),
    ARTISAN(ClassRace.DWARF, ClassType.FIGHTER, 1, "Artisan", DWARVEN_FIGHTER),
    WARSMITH(ClassRace.DWARF, ClassType.FIGHTER, 2, "Warsmith", ARTISAN),
    DUMMY_1(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 1", null),
    DUMMY_2(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 2", null),
    DUMMY_3(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 3", null),
    DUMMY_4(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 4", null),
    DUMMY_5(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 5", null),
    DUMMY_6(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 6", null),
    DUMMY_7(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 7", null),
    DUMMY_8(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 8", null),
    DUMMY_9(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 9", null),
    DUMMY_10(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 10", null),
    DUMMY_11(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 11", null),
    DUMMY_12(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 12", null),
    DUMMY_13(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 13", null),
    DUMMY_14(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 14", null),
    DUMMY_15(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 15", null),
    DUMMY_16(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 16", null),
    DUMMY_17(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 17", null),
    DUMMY_18(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 18", null),
    DUMMY_19(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 19", null),
    DUMMY_20(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 20", null),
    DUMMY_21(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 21", null),
    DUMMY_22(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 22", null),
    DUMMY_23(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 23", null),
    DUMMY_24(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 24", null),
    DUMMY_25(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 25", null),
    DUMMY_26(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 26", null),
    DUMMY_27(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 27", null),
    DUMMY_28(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 28", null),
    DUMMY_29(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 29", null),
    DUMMY_30(ClassRace.DUMMY, ClassType.DUMMY, -1, "dummy 30", null),
    DUELIST(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Duelist", GLADIATOR),
    DREADNOUGHT(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Dreadnought", WARLORD),
    PHOENIX_KNIGHT(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Phoenix Knight", PALADIN),
    HELL_KNIGHT(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Hell Knight", DARK_AVENGER),
    SAGGITARIUS(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Sagittarius", HAWKEYE),
    ADVENTURER(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Adventurer", TREASURE_HUNTER),
    ARCHMAGE(ClassRace.HUMAN, ClassType.MYSTIC, 3, "Archmage", SORCERER),
    SOULTAKER(ClassRace.HUMAN, ClassType.MYSTIC, 3, "Soultaker", NECROMANCER),
    ARCANA_LORD(ClassRace.HUMAN, ClassType.MYSTIC, 3, "Arcana Lord", WARLOCK),
    CARDINAL(ClassRace.HUMAN, ClassType.PRIEST, 3, "Cardinal", BISHOP),
    HIEROPHANT(ClassRace.HUMAN, ClassType.PRIEST, 3, "Hierophant", PROPHET),
    EVAS_TEMPLAR(ClassRace.ELF, ClassType.FIGHTER, 3, "Eva's Templar", TEMPLE_KNIGHT),
    SWORD_MUSE(ClassRace.ELF, ClassType.FIGHTER, 3, "Sword Muse", SWORD_SINGER),
    WIND_RIDER(ClassRace.ELF, ClassType.FIGHTER, 3, "Wind Rider", PLAINS_WALKER),
    MOONLIGHT_SENTINEL(ClassRace.ELF, ClassType.FIGHTER, 3, "Moonlight Sentinel", SILVER_RANGER),
    MYSTIC_MUSE(ClassRace.ELF, ClassType.MYSTIC, 3, "Mystic Muse", SPELLSINGER),
    ELEMENTAL_MASTER(ClassRace.ELF, ClassType.MYSTIC, 3, "Elemental Master", ELEMENTAL_SUMMONER),
    EVAS_SAINT(ClassRace.ELF, ClassType.PRIEST, 3, "Eva's Saint", ELVEN_ELDER),
    SHILLIEN_TEMPLAR(ClassRace.DARK_ELF, ClassType.FIGHTER, 3, "Shillien Templar", SHILLIEN_KNIGHT),
    SPECTRAL_DANCER(ClassRace.DARK_ELF, ClassType.FIGHTER, 3, "Spectral Dancer", BLADEDANCER),
    GHOST_HUNTER(ClassRace.DARK_ELF, ClassType.FIGHTER, 3, "Ghost Hunter", ABYSS_WALKER),
    GHOST_SENTINEL(ClassRace.DARK_ELF, ClassType.FIGHTER, 3, "Ghost Sentinel", PHANTOM_RANGER),
    STORM_SCREAMER(ClassRace.DARK_ELF, ClassType.MYSTIC, 3, "Storm Screamer", SPELLHOWLER),
    SPECTRAL_MASTER(ClassRace.DARK_ELF, ClassType.MYSTIC, 3, "Spectral Master", PHANTOM_SUMMONER),
    SHILLIEN_SAINT(ClassRace.DARK_ELF, ClassType.PRIEST, 3, "Shillien Saint", SHILLIEN_ELDER),
    TITAN(ClassRace.ORC, ClassType.FIGHTER, 3, "Titan", DESTROYER),
    GRAND_KHAVATARI(ClassRace.ORC, ClassType.FIGHTER, 3, "Grand Khavatari", TYRANT),
    DOMINATOR(ClassRace.ORC, ClassType.MYSTIC, 3, "Dominator", OVERLORD),
    DOOMCRYER(ClassRace.ORC, ClassType.MYSTIC, 3, "Doom Cryer", WARCRYER),
    FORTUNE_SEEKER(ClassRace.DWARF, ClassType.FIGHTER, 3, "Fortune Seeker", BOUNTY_HUNTER),
    MAESTRO(ClassRace.DWARF, ClassType.FIGHTER, 3, "Maestro", WARSMITH);


    val id : Int get() = ordinal
    private val availableSubClasses= mutableSetOf<ClassId>()

    override fun toString(): String {
        return className
    }

    fun childOf(classId: ClassId): Boolean {
        if (parent == null) return false
        return if (parent == classId) true else parent.childOf(classId)
    }

    fun equalsOrChildOf(classId: ClassId): Boolean {
        return this == classId || childOf(classId)
    }

    private fun createSubclasses() {
        // only 2nd class level can have subclasses
        if (level != 2) {
            return
        }

        for (classId in VALUES) {
            // only second classes may be taken as subclass
            if (classId.level != 2) continue

            // Overlord, Warsmith or self class may never be taken as subclass
            if (classId == OVERLORD || classId == WARSMITH || classId == this) continue

            // Elves may not sub Dark Elves and vice versa
            if (this.race === ClassRace.ELF && classId.race === ClassRace.DARK_ELF ||
                this.race === ClassRace.DARK_ELF && classId.race === ClassRace.ELF
            ) {
                continue
            }
            availableSubClasses.add(classId)
        }
        when (this) {
            DARK_AVENGER, PALADIN, TEMPLE_KNIGHT, SHILLIEN_KNIGHT -> availableSubClasses.removeAll(EnumSet.of(DARK_AVENGER, PALADIN, TEMPLE_KNIGHT, SHILLIEN_KNIGHT))
            TREASURE_HUNTER, ABYSS_WALKER, PLAINS_WALKER -> availableSubClasses.removeAll(EnumSet.of(TREASURE_HUNTER, ABYSS_WALKER, PLAINS_WALKER))
            HAWKEYE, SILVER_RANGER, PHANTOM_RANGER -> availableSubClasses.removeAll(EnumSet.of(HAWKEYE, SILVER_RANGER, PHANTOM_RANGER))
            WARLOCK, ELEMENTAL_SUMMONER, PHANTOM_SUMMONER -> availableSubClasses.removeAll(EnumSet.of(WARLOCK, ELEMENTAL_SUMMONER, PHANTOM_SUMMONER))
            SORCERER, SPELLSINGER, SPELLHOWLER -> availableSubClasses.removeAll(EnumSet.of(SORCERER, SPELLSINGER, SPELLHOWLER))
        }
    }

    companion object {
        val VALUES: Array<ClassId> = values()

        /**
         * Returns set of subclasses available for given [Player].<br></br>
         * 1) If the race of your main class is Elf or Dark Elf, you may not select each class as a subclass to the other class.<br></br>
         * 2) You may not select Overlord and Warsmith class as a subclass.<br></br>
         * 3) You may not select a similar class as the subclass. The occupations classified as similar classes are as follows:<br></br>
         * Paladin, Dark Avenger, Temple Knight and Shillien Knight Treasure Hunter, Plainswalker and Abyss Walker Hawkeye, Silver Ranger and Phantom Ranger Warlock, Elemental Summoner and Phantom Summoner Sorcerer, Spellsinger and Spellhowler
         * @param player : The [Player] to make checks on.
         * @return EnumSet<ClassId> : Available subclasses for given player.
        </ClassId> */
//        fun getAvailableSubclasses(player: Player): EnumSet<ClassId>? {
//            var classId: ClassId? = VALUES[player.getBaseClass()]
//            if (classId!!._level < 2) return null
//
//            // handle 3rd level class
//            if (classId._level == 3) classId = classId.parent
//            return EnumSet.copyOf(classId!!._subclasses)
//        }

        init {
            // create subclass lists
            VALUES.forEach {
                it.createSubclasses()
            }
        }
    }
}