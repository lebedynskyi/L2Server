package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class CharSelected(
    private val player: PlayerObject,
    private val playOKId1: Int
) : GameServerPacket() {
    override fun write() {
        writeC(0x15)

        writeS(player.name)
        writeD(player.objectId)
        writeS(player.title)
        writeD(playOKId1)
        writeD(player.clanId)

        writeD(0x00) // unknown

        writeD(player.appearance.sex.ordinal)
        writeD(player.raceId)
        writeD(player.classId)

        writeD(0x01)

        writeD(player.position.x)
        writeD(player.position.y)
        writeD(player.position.z)
        writeF(player.stats.curHp)
        writeF(player.stats.curMp)
        writeD(player.stats.sp)
        writeQ(player.stats.exp)
        writeD(player.stats.level)
        writeD(player.karma)
        writeD(player.pkKills)
        writeD(player.stats.getINT())
        writeD(player.stats.getSTR())
        writeD(player.stats.getCON())
        writeD(player.stats.getMEN())
        writeD(player.stats.getDEX())
        writeD(player.stats.getWIT())

        for (i in 0..29) {
            // 30 zero digits
            writeD(0x00)
        }

        writeD(0x00) // c3 work
        writeD(0x00) // c3 work

        writeD(15506)

        writeD(0x00) // c3

        writeD(player.classId)

        writeD(0x00) // c3 InspectorBin
        writeD(0x00) // c3
        writeD(0x00) // c3
        writeD(0x00) // c3
    }
}