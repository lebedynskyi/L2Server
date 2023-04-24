package lineage.vetal.server.core.model


data class AccountInfo(
    val id: String,
    val account: String,
    val password: String? = null
)