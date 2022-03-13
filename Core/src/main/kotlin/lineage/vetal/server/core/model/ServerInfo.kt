package lineage.vetal.server.core.model

data class ServerInfo(
    val clientId: Int,
    val ip: String,
    val port: Int,
    val ageLimit: Int,
    val isPvp: Boolean,
    val onlineCount: Int,
    val maxOnline: Int,
    val isOnline: Boolean
)