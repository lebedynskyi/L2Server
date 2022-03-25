package lineage.vetal.server.core.model

data class AccountInfo(
    val account: String,
) {

    constructor(account: String, pass: String) : this(account) {
        password = pass
    }

    var password: String? = null
}