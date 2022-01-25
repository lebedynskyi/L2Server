package lineage.vetal.server.core.settings

import kotlinx.serialization.Serializable

@Serializable
data class NetworkConfig(
    val hostname: String,
    val port: Int
)