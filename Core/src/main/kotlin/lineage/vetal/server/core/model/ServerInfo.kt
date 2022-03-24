package lineage.vetal.server.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ServerInfo(
    val id: Int,
    val ip: String,
    val port: Int,
    val ageLimit: Int,
    val isPvp: Boolean,
    val maxOnline: Int
) {
    lateinit var bridgeKey: String

    @Transient
    var serverStatus: ServerStatus? = null
}

data class ServerStatus(
    val id: Int,
    var onlineCount: Int,
    var isOnline: Boolean
)