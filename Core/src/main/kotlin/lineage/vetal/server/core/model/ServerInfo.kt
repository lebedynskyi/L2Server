package lineage.vetal.server.core.model

data class ServerStatus(
    val id: Int,
    var onlineCount: Int,
    var isOnline: Boolean
)