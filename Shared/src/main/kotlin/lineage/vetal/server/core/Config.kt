package lineage.vetal.server.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import lineage.vetal.server.core.model.ServerStatus

@Serializable
data class ConfigDataBase(
    val url: String,
    val user: String,
    val password: String,
    val timeOut: Long
)

@Serializable
data class ConfigNetwork(
    val hostname: String,
    val port: Int
)

@Serializable
data class ConfigRegisteredServer(
    val id: Int,
    val ip: String,
    val port: Int,
    val ageLimit: Int,
    val isPvp: Boolean,
    val maxOnline: Int,
    val bridgeKey: String
) {

    @Transient
    var serverStatus: ServerStatus? = null
}