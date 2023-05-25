package lineage.vetal.server.game.gameserver.packet.server

import lineage.vetal.server.game.gameserver.packet.GameServerPacket


class SSQInfo private constructor(
    private val state: Int
) : GameServerPacket() {
    override val opCode: Byte = 0xf8.toByte()

    override fun write() {
        writeH(state)
    }

    companion object {
        val REGULAR_SKY_PACKET = SSQInfo(256)
        val DUSK_SKY_PACKET = SSQInfo(257)
        val DAWN_SKY_PACKET = SSQInfo(258)
        val RED_SKY_PACKET = SSQInfo(259)
//
//        fun sendSky(): SSQInfo {
//            if (SevenSignsManager.getInstance().isSealValidationPeriod()) {
//                val winningCabal: CabalType = SevenSignsManager.getInstance().getWinningCabal()
//                if (winningCabal === CabalType.DAWN) return DAWN_SKY_PACKET
//                if (winningCabal === CabalType.DUSK) return DUSK_SKY_PACKET
//            }
//            return REGULAR_SKY_PACKET
//        }
    }
}