package lineage.vetal.server.game

import com.charleskorn.kaml.Yaml
import lineage.vetal.server.core.ConfigNetwork
import kotlinx.serialization.Serializable
import lineage.vetal.server.core.ConfigDataBase
import lineage.vetal.server.core.ConfigRegisteredServer
import java.io.File
import java.io.InputStream

@Serializable
class ConfigGame(
    val serverInfo: ConfigRegisteredServer,
    val bridgeServer: ConfigNetwork,
    val dataBaseConfig: ConfigDataBase
) {
    companion object Builder {
        fun read(path: String): lineage.vetal.server.game.ConfigGame {
            return lineage.vetal.server.game.ConfigGame.Builder.read(File(path))
        }

        fun read(file: File): lineage.vetal.server.game.ConfigGame {
            return lineage.vetal.server.game.ConfigGame.Builder.read(file.inputStream())
        }

        fun read(stream: InputStream): lineage.vetal.server.game.ConfigGame {
            return Yaml.default.decodeFromStream(serializer(), stream)
        }
    }
}