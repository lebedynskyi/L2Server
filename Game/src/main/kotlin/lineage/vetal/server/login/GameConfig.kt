package lineage.vetal.server.login

import com.charleskorn.kaml.Yaml
import lineage.vetal.server.core.NetworkConfig
import kotlinx.serialization.Serializable
import lineage.vetal.server.core.model.ServerInfo
import java.io.File
import java.io.InputStream

@Serializable
class GameConfig(
    val bridgeServer: NetworkConfig,
    val serverInfo: ServerInfo
) {
    companion object Builder {
        fun read(path: String): GameConfig {
            return read(File(path))
        }

        fun read(file: File): GameConfig {
            return read(file.inputStream())
        }

        fun read(stream: InputStream): GameConfig {
            return Yaml.default.decodeFromStream(serializer(), stream)
        }
    }
}