package lineage.vetal.server.login

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.serialization.Serializable
import lineage.vetal.server.core.ConfigDataBase
import lineage.vetal.server.core.ConfigNetwork
import lineage.vetal.server.core.ConfigRegisteredServer
import java.io.File
import java.io.InputStream

@Serializable
class ConfigLoginServer(
    val lobbyConfig: ConfigLoginLobby,
    val clientServer: ConfigNetwork,
    val bridgeServer: ConfigNetwork,
    val registeredServers: List<ConfigRegisteredServer>,
    val dataBaseConfig: ConfigDataBase
) {
    companion object Builder {
        fun read(path: String): ConfigLoginServer {
            return read(File(path))
        }

        fun read(file: File): ConfigLoginServer {
            return read(file.inputStream())
        }

        fun read(stream: InputStream): ConfigLoginServer {
            return Yaml.default.decodeFromStream(serializer(), stream)
        }
    }
}

@Serializable
data class ConfigLoginLobby(
    val maxCount: Int,
    val showLicense: Boolean,
    val autoRegistration: Boolean
)