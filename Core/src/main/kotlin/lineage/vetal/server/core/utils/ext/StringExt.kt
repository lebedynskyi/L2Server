package lineage.vetal.server.core.utils.ext

private val lettersRegex = "^[a-zA-Z0-9]*$".toRegex()

fun String.isValidNickName(): Boolean {
    return matches(lettersRegex)
}