package lineage.vetal.server.login.gameclient.packet.server

import lineage.vetal.server.login.gameclient.packet.GameServerPacket


class SSQInfo private constructor(
    private val _state: Int
) : GameServerPacket() {
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

    override fun write() {
        writeC(0xf8)
        writeH(_state)
    }
}