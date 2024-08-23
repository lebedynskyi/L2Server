package lineage.vetal.server.game.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.DBDao
import lineage.vetal.server.core.utils.ext.toBoolean
import lineage.vetal.server.game.game.model.CharSelectionSlot
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.player.Appearance
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.player.CharacterSex
import lineage.vetal.server.game.game.model.player.status.PlayerStatus
import lineage.vetal.server.game.game.model.template.pc.CharTemplate
import lineage.vetal.server.game.db.sql.CharactersSQL

class CharactersDao(
    connection: DBConnection,
    private val charTemplates: Map<Int, CharTemplate>
) : DBDao(connection) {
    fun insertCharacter(player: PlayerObject): Boolean {
        return insertOrUpdate(CharactersSQL.INSERT_CHARACTER_SQL) {
            it.setString(1, player.id)
            it.setInt(2, player.objectId)
            it.setString(3, player.accountId)
            it.setString(4, player.name)
            it.setInt(5, player.stats.level)
            it.setInt(6, player.stats.maxHp)
            it.setDouble(7, player.stats.curHp)
            it.setInt(8, player.stats.maxCp)
            it.setDouble(9, player.stats.curCp)
            it.setInt(10, player.stats.maxMp)
            it.setDouble(11, player.stats.curMp)
            it.setLong(12, player.stats.exp)
            it.setInt(13, player.stats.sp)
            it.setInt(14, player.appearance.face)
            it.setInt(15, player.appearance.hairStyle)
            it.setInt(16, player.appearance.hairColor)
            it.setInt(17, player.appearance.sex.ordinal)
            it.setInt(18, player.karma)
            it.setInt(19, player.pvpKills)
            it.setInt(20, player.pkKills)
            it.setInt(21, player.clanId)
            it.setInt(22, player.template.charClass.race.ordinal)
            it.setInt(23, player.template.charClass.ordinal)
            it.setLong(24, player.deleteTimer)
            it.setInt(25, if (player.hasDwarvesCraft) 1 else 0)
            it.setString(26, player.title)
            it.setInt(27, player.accessLevel)
            it.setInt(28, player.isOnlineInt)
            it.setInt(29, if (player.isIn7sDungeon) 1 else 0)
            it.setInt(30, player.clanPrivileges)
            it.setInt(31, if (player.wantsPeace) 1 else 0)
            it.setInt(32, player.template.charClass.id)
            it.setInt(33, if (player.isNoble) 1 else 0)
            it.setInt(34, player.position.x)
            it.setInt(35, player.position.y)
            it.setInt(36, player.position.z)
        }
    }

    fun updateCoordinates(objId: Int, location: SpawnPosition): Boolean {
        return insertOrUpdate(CharactersSQL.UPDATE_COORDINATES_SQL) {
            it.setInt(1, location.x)
            it.setInt(2, location.y)
            it.setInt(3, location.z)
            it.setInt(4, objId)
        }
    }

    fun updateLastAccess(objId: Int, lastAccess: Long): Boolean {
        return insertOrUpdate(CharactersSQL.UPDATE_lAST_ACCESS_SQL) {
            it.setLong(1, lastAccess)
            it.setInt(2, objId)
        }
    }

    fun getCharSlots(accountId: String): List<CharSelectionSlot> {
        return queryList(CharactersSQL.SELECT_CHARACTER_SLOTS_SQL,
            onPrepare = { it.setString(1, accountId) }) {
            CharSelectionSlot(
                id = it.getString(1),
                objectId = it.getInt(2),
                name = it.getString(3),
                level = it.getInt(4),
                maxHp = it.getDouble(5),
                currentHp = it.getDouble(6),
                maxMp = it.getDouble(7),
                currentMp = it.getDouble(8),
                face = it.getInt(9),
                hairStyle = it.getInt(10),
                hairColor = it.getInt(11),
                sex = CharacterSex.values()[it.getInt(12)],
                x = it.getInt(13),
                y = it.getInt(14),
                z = it.getInt(15),
                exp = it.getLong(16),
                sp = it.getInt(17),
                karma = it.getInt(18),
                pvPKills = it.getInt(19),
                pkKills = it.getInt(20),
                clanId = it.getInt(21),
                race = it.getInt(22),
                classId = it.getInt(23),
                deleteTimer = it.getLong(24),
                title = it.getString(25),
                accessLevel = it.getInt(26),
                lastAccess = it.getLong(27),
                baseClassId = it.getInt(28),
                augmentationId = 0
            )
        }
    }

    fun getCharacter(charId: String): PlayerObject? {
        return querySingle(CharactersSQL.SELECT_CHARACTER_SQL,
            onPrepare = { it.setString(1, charId) }) {
            PlayerObject(
                it.getInt(1),
                it.getString(2),
                it.getString(3),
                Appearance(it.getInt(14), it.getInt(15), it.getInt(16), CharacterSex.values()[it.getInt(17)]),
                it.getString(4),
                charTemplates.getValue(it.getInt(22)),
                SpawnPosition(it.getInt(33), it.getInt(34), it.getInt(35), 0)
            ).apply {
                stats = PlayerStatus(template).apply {
                    level = it.getInt(5)
                    maxHp = it.getInt(6)
                    curHp = it.getDouble(7)
                    maxCp = it.getInt(8)
                    curCp = it.getDouble(9)
                    maxMp = it.getInt(10)
                    curMp = it.getDouble(11)
                    exp = it.getLong(12)
                    sp = it.getInt(13)
                }
                karma = it.getInt(18)
                pvpKills = it.getInt(19)
                pkKills = it.getInt(20)
                clanId = it.getInt(21)
                deleteTimer = it.getLong(23)
                hasDwarvesCraft = it.getInt(24).toBoolean()
                title = it.getString(25)
                accessLevel = it.getInt(26)
                isOnlineInt = it.getInt(27)
                isIn7sDungeon = it.getInt(28).toBoolean()
                clanPrivileges = it.getInt(29)
                wantsPeace = it.getInt(30).toBoolean()
//                charTemplate.charClass.id = it.getInt(31)
                isNoble = it.getInt(32).toBoolean()
                isHero = it.getInt(36).toBoolean()
            }
        }
    }

    fun getAllObjectIds(): List<Int> {
        return queryList(CharactersSQL.SELECT_CHARACTERS_IDS_SQL) {
            it.getInt(1)
        }
    }
}
