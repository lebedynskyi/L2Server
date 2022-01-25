package lineage.vetal.server.core.logs

fun writeDebug(tag: String, msg: Any) {
    println("$tag: $msg")
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