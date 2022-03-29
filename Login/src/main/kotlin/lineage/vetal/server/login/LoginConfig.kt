package lineage.vetal.server.login

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import lineage.vetal.server.core.DataBaseConfig
import lineage.vetal.server.core.NetworkConfig
import lineage.vetal.server.core.ServerInfo
import java.io.File
import java.io.InputStream

@Serializable
class LoginConfig(
    val lobbyConfig: LobbyConfig,
    val clientServer: NetworkConfig,
    val bridgeServer: NetworkConfig,
    val registeredServers: Array<ServerInfo>,
    val dataBaseConfig: DataBaseConfig
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