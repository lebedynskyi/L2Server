package lineage.vetal.server.core.model

import java.util.UUID

data class AccountInfo(
    val id: UUID,
    val account: String,
    val password: String? = null
)