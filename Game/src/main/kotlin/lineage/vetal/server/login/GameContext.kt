package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.db.GameDatabase
import lineage.vetal.server.login.game.GameWorld
import lineage.vetal.server.login.game.model.template.CharacterTemplate
import lineage.vetal.server.login.xml.CharacterTemplatesXMLReader
import java.io.File

private const val PATH_SERVER_CONFIG = "game/config/Server.yaml"
private const val PATH_CLASSES_XML = "game/xml/classes"

class GameContext(
    dataFolder: String
) {
    private val TAG = "GameContext"

    val config: GameConfig
    val gameWorld: GameWorld
    val characterTemplates: Map<Int, CharacterTemplate>
    val gameDatabase: GameDatabase
    val threadPool: ThreadPool

    // TODO add some additional xml reading NPC?
    // TODO add Game timer thread
    init {
        writeSection(TAG)
        writeInfo(TAG, "Reading configs")
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")

        config = GameConfig.read(serverConfigFile)
        gameWorld = GameWorld()

        writeInfo(TAG, "Initialize thread pool")
        threadPool = ThreadPool()

        val charsXmlFolder = File(dataFolder, PATH_CLASSES_XML)
        writeInfo(TAG, "Reading classes templates from ${charsXmlFolder.absolutePath}")
        characterTemplates = CharacterTemplatesXMLReader(charsXmlFolder.absolutePath).load()

        writeInfo(TAG, "Initialize database")
        gameDatabase = GameDatabase(config.dataBaseConfig, characterTemplates)
    }
}