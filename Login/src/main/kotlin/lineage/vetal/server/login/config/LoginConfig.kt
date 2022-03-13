package lineage.vetal.server.login.config

import com.charleskorn.kaml.Yaml
import com.sun.org.apache.xpath.internal.operations.Bool
import kotlinx.serialization.Serializable
import lineage.vetal.server.core.config.NetworkConfig
import java.io.File
import java.io.InputStream

@Serializable
data class LoginConfig(
    val clientServer: NetworkConfig,
    val gameServer: NetworkConfig,
    val lobbyConfig: LobbyConfig
) {
    companion object Builder {
        fun read(path: String): LoginConfig {
            return read(File(path))
        }

        fun read(file: File): LoginConfig {
            return read(file.inputStream())
        }

        fun read(stream: InputStream): LoginConfig {
            return Yaml.default.decodeFromStream(serializer(), stream)
        }
    }
}

@Serializable
data class LobbyConfig(
    val maxCount: Int,
    val showLicense: Boolean
)