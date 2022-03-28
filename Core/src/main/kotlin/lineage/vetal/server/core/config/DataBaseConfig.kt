package lineage.vetal.server.core.config

@kotlinx.serialization.Serializable
data class DataBaseConfig(
    val url: String,
    val user: String,
    val password: String,
)