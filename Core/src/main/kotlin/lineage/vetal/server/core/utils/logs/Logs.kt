package lineage.vetal.server.core.utils.logs

fun writeDebug(tag: String, msg: Any) {
    println("D: $tag: $msg")
}

fun writeInfo(tag: String, msg: Any) {
    println("I: $tag: $msg")
}

fun writeError(tag: String, msg: Any, e: Throwable) {
    System.err.println("I: $tag: $msg")
    e.printStackTrace()
}

fun writeSection(section: String, char: Char = '-') {
    print("[$section]")

    var symbolsLeft = 80 - section.length
    while (symbolsLeft >= 0) {
        symbolsLeft--
        print(char)
    }
    println()
}