package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.core.server.SendablePacket

class QuestList: SendablePacket() {
    override fun write() {
        writeC(0x80)
        writeH(0) // - the size of quests list

        // TODO add quests here
//        writeH(_questStates.size)
//        for (qs in _questStates) {
//            writeD(qs.getQuest().getQuestId())
//            writeD(qs.getFlags())
//        }
    }
}