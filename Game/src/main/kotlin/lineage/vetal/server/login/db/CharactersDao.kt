package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.Dao
import lineage.vetal.server.core.utils.ext.toBoolean
import lineage.vetal.server.login.game.model.CharSelectionSlot
import lineage.vetal.server.core.model.location.SpawnLocation
import lineage.vetal.server.login.game.model.player.Appearance
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.game.model.player.Sex
import lineage.vetal.server.login.game.model.player.status.PlayerStatus
import lineage.vetal.server.login.game.model.template.CharacterTemplate
import java.util.*

class CharactersDao(
    db: DBConnection,
    private val charTemplates: MutableMap<Int, CharacterTemplate>
) : Dao(db) {
    private val INSERT_CHARACTER_SQL =
        "INSERT INTO characters (id,account_id,char_name,level,maxHp,curHp,maxCp,curCp,maxMp,curMp,exp,sp,face,hairStyle,hairColor,sex,karma,pvpkills,pkkills,clanid,race,classid,deletetime,cancraft,title,accesslevel,online,isin7sdungeon,clan_privs,wantspeace,base_class,nobless,x,y,z) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
    private val SELECT_CHARACTER_SLOTS_SQL =
        "SELECT obj_Id, char_name, level, maxHp, curHp, maxMp, curMp, face, hairStyle, hairColor, sex, x, y, z, exp, sp, karma, pvpkills, pkkills, clanid, race, classid, deletetime, title, accesslevel, lastAccess, base_class, id FROM characters WHERE account_id=?"
    private val SELECT_CHARACTER_SQL =
        "SELECT id,char_name,account_id,level,maxHp,curHp,maxCp,curCp,maxMp,curMp,exp,sp,face,hairStyle,hairColor,sex,karma,pvpkills,pkkills,clanid,classid,deletetime,cancraft,title,accesslevel,online,isin7sdungeon,clan_privs,wantspeace,base_class,nobless,x,y,z from characters WHERE id=?"

    fun insertCharacter(player: Player): Boolean {
        return insertOrUpdate(INSERT_CHARACTER_SQL) {
            it.setString(1, player.id.toString())
            it.setString(2, player.accountId.toString())
            it.setString(3, player.name)
            it.setInt(4, player.status.level)
            it.setInt(5, player.status.maxHp)
            it.setDouble(6, player.status.hp)
            it.setInt(7, player.status.maxCp)
            it.setDouble(8, player.status.cp)
            it.setInt(9, player.status.maxMp)
            it.setDouble(10, player.status.mp)
            it.setLong(11, player.status.exp)
            it.setInt(12, player.status.sp)
            it.setInt(13, player.appearance.face)
            it.setInt(14, player.appearance.hairStyle)
            it.setInt(15, player.appearance.hairColor)
            it.setInt(16, player.appearance.sex.ordinal)
            it.setInt(17, player.karma)
            it.setInt(18, player.pvpKills)
            it.setInt(19, player.pkKills)
            it.setInt(20, player.clanId)
            it.setInt(21, player.charTemplate.charClass.race.ordinal)
            it.setInt(22, player.charTemplate.charClass.ordinal)
            it.setLong(23, player.deleteTimer)
            it.setInt(24, if (player.hasDwarvenCraft) 1 else 0)
            it.setString(25, player.title)
            it.setInt(26, player.accessLevel)
            it.setInt(27, player.isOnlineInt)
            it.setInt(28, if (player.isIn7sDungeon) 1 else 0)
            it.setInt(29, player.clanPrivileges)
            it.setInt(30, if (player.wantsPeace) 1 else 0)
            it.setInt(31, player.baseClassId)
            it.setInt(32, if (player.isNoble) 1 else 0)
            it.setInt(33, player.position.x)
            it.setInt(34, player.position.y)
            it.setInt(35, player.position.z)
        }
    }

    fun getCharSlots(id: UUID): List<CharSelectionSlot> {
        return query(SELECT_CHARACTER_SLOTS_SQL) {
            it.setString(1, id.toString())
        }.listOrEmpty {
            CharSelectionSlot(
                objectId = it.getInt(1),
                name = it.getString(2),
                level = it.getInt(3),
                maxHp = it.getDouble(4),
                currentHp = it.getDouble(5),
                maxMp = it.getDouble(6),
                currentMp = it.getDouble(7),
                face = it.getInt(8),
                hairStyle = it.getInt(9),
                hairColor = it.getInt(10),
                sex = Sex.values()[it.getInt(11)],
                x = it.getInt(12),
                y = it.getInt(13),
                z = it.getInt(14),
                exp = it.getLong(15),
                sp = it.getInt(16),
                karma = it.getInt(17),
                pvPKills = it.getInt(18),
                pkKills = it.getInt(19),
                clanId = it.getInt(20),
                race = it.getInt(21),
                classId = it.getInt(22),
                deleteTimer = it.getLong(23),
                title = it.getString(24),
                accessLevel = it.getInt(25),
                lastAccess = it.getLong(26),
                baseClassId = it.getInt(27),
                id = UUID.fromString(it.getString(28)),
                augmentationId = 0
            )
        }
    }

    fun getCharacter(id: UUID): Player? {
        return query(SELECT_CHARACTER_SQL) {
            it.setString(1, id.toString())
        }.firstOrNull {
            val classId = it.getInt(21)
            val template = charTemplates.getValue(classId)
            val location = SpawnLocation(it.getInt(32), it.getInt(33), it.getInt(34), 0)
            val appearance = Appearance(it.getInt(13), it.getInt(14), it.getInt(15), Sex.values()[it.getInt(16)])
            Player(
                UUID.fromString(it.getString(1)),
                it.getString(2),
                UUID.fromString(it.getString(3)),
                template,
                appearance,
                location
            ).apply {
                status = PlayerStatus(template).apply {
                    level = it.getInt(4)
                    maxHp = it.getInt(5)
                    hp = it.getDouble(6)
                    maxCp = it.getInt(7)
                    cp = it.getDouble(8)
                    maxMp = it.getInt(9)
                    mp = it.getDouble(10)
                    exp = it.getLong(11)
                    sp = it.getInt(12)
                }
                karma = it.getInt(17)
                pvpKills = it.getInt(18)
                pkKills = it.getInt(19)
                clanId = it.getInt(20)
                deleteTimer = it.getLong(22)
                hasDwarvenCraft = it.getInt(23).toBoolean()
                title = it.getString(24)
                accessLevel = it.getInt(25)
                isOnlineInt = it.getInt(26)
                isIn7sDungeon = it.getInt(27).toBoolean()
                clanPrivileges = it.getInt(28)
                wantsPeace = it.getInt(29).toBoolean()
                baseClassId = it.getInt(30)
                isNoble = it.getInt(31).toBoolean()
            }
        }
    }
}
