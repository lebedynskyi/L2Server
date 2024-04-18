package lineage.vetal.server.core.utils.ext


fun String?.ifNullOrBlank(block: () -> String) = if (isNullOrBlank()) block() else this