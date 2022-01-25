package lineage.vetal.server.login.settings

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import lineage.vetal.server.core.settings.NetworkConfig
import java.io.File
import java.io.InputStream

@Serializable
data class LoginServerConfig(
    val clientServer: NetworkConfig,
    val gameServer: NetworkConfig
) {
    companion object Builder {
        fun read(path: String): LoginServerConfig {
            return read(File(path))
        }

        fun read(file: File): LoginServerConfig {
            return read(file.inputStream())
        }

        fun read(stream: InputStream): LoginServerConfig {
            return Yaml.default.decodeFromStream(serializer(), stream)
        }
    }
}