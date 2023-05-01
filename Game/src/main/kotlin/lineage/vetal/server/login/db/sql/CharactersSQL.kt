package lineage.vetal.server.login.db.sql

class CharactersSQL {
    companion object {
        const val INSERT_CHARACTER_SQL = "" +
                "INSERT INTO characters " +
                "(id,obj_Id,account_id,char_name,level,maxHp,curHp,maxCp,curCp,maxMp,curMp,exp,sp,face,hairStyle,hairColor,sex,karma,pvpkills,pkkills,clanid,race,classid,deletetime,cancraft,title,accesslevel,online,isin7sdungeon,clan_privs,wantspeace,base_class,nobless,x,y,z) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
        const val SELECT_CHARACTER_SLOTS_SQL = "" +
                "SELECT id, obj_Id, char_name, level, maxHp, curHp, maxMp, curMp, face, hairStyle, hairColor, sex, x, y, z, exp, sp, karma, pvpkills, pkkills, clanid, race, classid, deletetime, title, accesslevel, lastAccess, base_class " +
                "FROM characters " +
                "WHERE account_id=?"
        const val SELECT_CHARACTER_SQL = "" +
                "SELECT id,account_id,obj_Id,char_name,level,maxHp,curHp,maxCp,curCp,maxMp,curMp,exp,sp,face,hairStyle,hairColor,sex,karma,pvpkills,pkkills,clanid,classid,deletetime,cancraft,title,accesslevel,online,isin7sdungeon,clan_privs,wantspeace,base_class,nobless,x,y,z,hero " +
                "FROM characters " +
                "WHERE id=?"
        const val SELECT_CHARACTERS_IDS_SQL = "" +
                "SELECT obj_Id " +
                "FROM characters "
        const val UPDATE_lAST_ACCESS_SQL = "" +
                "UPDATE characters SET lastAccess = ? " +
                "WHERE obj_Id = ?"
        const val UPDATE_COORDINATES_SQL = "" +
                "UPDATE characters SET x = ?, y = ?, z = ? " +
                "WHERE obj_Id = ?"

    }
}