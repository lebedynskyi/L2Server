package lineage.vetal.server.login.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.core.db.Dao
import lineage.vetal.server.login.game.model.player.Player

class CharactersDao(
    db: DBConnection
) : Dao(db) {
    private val INSERT_CHARACTER_SQL =
        "INSERT INTO characters (id,account_id,char_name,level,maxHp,curHp,maxCp,curCp,maxMp,curMp,face,hairStyle,hairColor,sex,exp,sp,karma,pvpkills,pkkills,clanid,race,classid,deletetime,cancraft,title,accesslevel,online,isin7sdungeon,clan_privs,wantspeace,base_class,nobless,power_grade,x,y,z) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

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
            it.setByte(11, player.appearance.face)
            it.setByte(12, player.appearance.hairStyle)
            it.setByte(13, player.appearance.hairColor)
            it.setInt(14, player.appearance.sex.ordinal)
            it.setLong(15, player.status.exp)
            it.setInt(16, player.status.sp)
            it.setInt(17, player.karma)
            it.setInt(18, player.pvpKills)
            it.setInt(19, player.pkKills)
            it.setInt(20, player.clanId)
            it.setInt(21, player.race)
            it.setInt(22, player.classId)
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
            it.setLong(33, 0)

            it.setInt(34, player.position.x)
            it.setInt(35, player.position.y)
            it.setInt(36, player.position.z)
        }
    }
//    fun getCharactersForAccount(accountId: UUID): List<Player> {
//        return query("") {
//
//        }.listOrEmpty {
//
//        }
//    }
}
