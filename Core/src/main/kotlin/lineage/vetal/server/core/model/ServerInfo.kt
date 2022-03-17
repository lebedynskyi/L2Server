package lineage.vetal.server.core.model

@kotlinx.serialization.Serializable
class ServerInfo(
    val id: Int,
    val ip: String,
    val port: Int,
    val ageLimit: Int,
    val isPvp: Boolean,
    val maxOnline: Int,
) {
    lateinit var blowFishKey: String
    var onlineCount: Int = 0
    var isOnline = false
}