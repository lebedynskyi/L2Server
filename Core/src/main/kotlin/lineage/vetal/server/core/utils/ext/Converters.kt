package lineage.vetal.server.core.utils.ext

fun Boolean?.toByte(): Int {
    return if (this == true) 0x01 else 0x00
}

fun Int.toBoolean(): Boolean {
    return this > 0
}