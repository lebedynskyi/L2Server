package lineage.vetal.server.login

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import lineage.vetal.server.core.config.NetworkConfig
import java.io.File
import java.io.InputStream

@Serializable
class LoginConfig(
    val lobbyConfig: LobbyConfig,
    val clientServer: NetworkConfig,
    val bridgeServer: NetworkConfig,
    val registeredServers: Array<RegisteredServer>
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

@Serializable
data class RegisteredServer(
    val id: Int,
    val blowFishKey: String
)