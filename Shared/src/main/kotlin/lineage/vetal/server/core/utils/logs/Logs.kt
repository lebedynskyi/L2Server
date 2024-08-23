package lineage.vetal.server.core.utils.logs

const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"

fun writeDebug(tag: String, msg: Any) {
    println("$ANSI_RESET D: $tag: $ANSI_RESET$msg")
}

fun writeInfo(tag: String, msg: Any) {
    println("$ANSI_BLUE I: $tag: $ANSI_RESET$msg")
}

fun writeError(tag: String, msg: Any, e: Throwable) {
    System.err.println("$ANSI_RED E: $tag: $ANSI_RESET$msg")
    e.printStackTrace()
}

fun writeError(tag: String, msg: Any) {
    System.err.println("$ANSI_RED E: $tag: $ANSI_RESET$msg")
}

fun writeSection(section: String, char: Char = '-') {
    print("[$ANSI_YELLOW$section$ANSI_RESET]")

    var symbolsLeft = 80 - section.length
    while (symbolsLeft >= 0) {
        symbolsLeft--
        print(char)
    }
    println()
}