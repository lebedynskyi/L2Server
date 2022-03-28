package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.game.GameWorld
import lineage.vetal.server.login.model.template.PlayerTemplate
import lineage.vetal.server.login.xml.PlayerTemplatesXMLReader
import java.io.File

private const val PATH_SERVER_CONFIG = "game/config/Server.yaml"
private const val PATH_GAME_CONFIG = "game/config/Game.yaml"
private const val PATH_CLASSES_XML = "game/xml/classes"

class GameContext(
    private val dataFolder: String
) {
    private val TAG = "GameContext"

    val config: GameConfig
    val gameWorld: GameWorld
    val templates: Map<Int, PlayerTemplate>

    init {
        writeSection(TAG)
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")

        config = GameConfig.read(serverConfigFile)
        gameWorld = GameWorld()

        val classesXmlFolder = File(dataFolder, PATH_CLASSES_XML)
        writeInfo(TAG, "Reading classes templates from${serverConfigFile.absolutePath}")
        templates = PlayerTemplatesXMLReader(classesXmlFolder.absolutePath).load()
    }
}