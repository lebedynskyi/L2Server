package lineage.vetal.server.login

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import lineage.vetal.server.core.ConfigDataBase
import lineage.vetal.server.core.ConfigNetwork
import lineage.vetal.server.core.ConfigRegisteredServer
import java.io.File
import java.io.InputStream

@Serializable
class ConfigLogin(
    val lobbyConfig: ConfigLoginLobby,
    val clientServer: ConfigNetwork,
    val bridgeServer: ConfigNetwork,
    val registeredServers: Array<ConfigRegisteredServer>,
    val dataBaseConfig: ConfigDataBase
) {
    companion object Builder {
        fun read(path: String): ConfigLogin {
            return read(File(path))
        }

        fun read(file: File): ConfigLogin {
            return read(file.inputStream())
        }

        fun read(stream: InputStream): ConfigLogin {
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