package lineage.vetal.server.core.utils.ext

private val playerNameRegex = "^[a-zA-Z0-9]{3,16}$".toRegex()

fun String.isValidPlayerName(): Boolean {
    return matches(playerNameRegex)
}

fun String?.ifNullOrBlank(block: () -> String) = if (isNullOrBlank()) block() else this