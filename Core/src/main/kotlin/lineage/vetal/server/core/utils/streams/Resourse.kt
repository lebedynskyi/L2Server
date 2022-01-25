package lineage.vetal.server.core.utils.streams

import java.io.IOException
import java.io.InputStream

fun openResource(path: String): InputStream {
    return object {}::class.java.classLoader.getResource(path)?.openStream()
        ?: throw IOException("Unable to read login server config in $path")
}