package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket

class QuestList : GameServerPacket() {
    override val opCode: Byte = 0x80.toByte()

    override fun write() {
        writeH(0) // - the size of quests list

        // TODO add quests here
//        writeH(_questStates.size)
//        for (qs in _questStates) {
//            writeD(qs.getQuest().getQuestId())
//            writeD(qs.getFlags())
//        }
    }
}