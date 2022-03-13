package lineage.vetal.server.core.config

import kotlinx.serialization.Serializable

@Serializable
data class NetworkConfig(
    val hostname: String,
    val port: Int
)