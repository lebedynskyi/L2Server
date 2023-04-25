package lineage.vetal.server.login.gameserver.packet.server

import lineage.vetal.server.login.game.model.player.PlayerObject
import lineage.vetal.server.login.gameserver.packet.GameServerPacket

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
        writeF(player.status.curHp)
        writeF(player.status.curMp)
        writeD(player.status.sp)
        writeQ(player.status.exp)
        writeD(player.status.level)
        writeD(player.karma)
        writeD(player.pkKills)
        writeD(player.status.getINT())
        writeD(player.status.getSTR())
        writeD(player.status.getCON())
        writeD(player.status.getMEN())
        writeD(player.status.getDEX())
        writeD(player.status.getWIT())

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