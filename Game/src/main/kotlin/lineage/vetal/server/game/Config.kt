package lineage.vetal.server.game

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import lineage.vetal.server.core.ConfigNetwork
import kotlinx.serialization.Serializable
import lineage.vetal.server.core.ConfigDataBase
import lineage.vetal.server.core.ConfigRegisteredServer
import java.io.File
import java.io.InputStream

@Serializable
class ConfigGameServer(
    val serverInfo: ConfigRegisteredServer,
    val bridgeServer: ConfigNetwork,
    val dataBaseConfig: ConfigDataBase
) {
    companion object Builder {
        fun read(path: String): ConfigGameServer {
            return read(File(path))
        }

        fun read(file: File): ConfigGameServer {
            return read(file.inputStream())
        }

        fun read(stream: InputStream): ConfigGameServer {
            return Yaml.default.decodeFromStream(serializer(), stream)
        }
    }
}