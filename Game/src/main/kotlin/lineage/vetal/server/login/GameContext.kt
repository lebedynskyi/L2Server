package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.db.GameDatabase
import lineage.vetal.server.login.game.GameWorld
import lineage.vetal.server.login.game.model.template.CharTemplate
import lineage.vetal.server.login.game.model.template.NpcTemplate
import lineage.vetal.server.login.xml.CharTemplatesXMLReader
import lineage.vetal.server.login.xml.NpcXMLReader
import java.io.File

private const val PATH_SERVER_CONFIG = "game/config/Server.yaml"
private const val PATH_CLASSES_XML = "game/xml/classes"
private const val NPCS_XML = "game/xml/npcs"

class GameContext(
    dataFolder: String
) {
    private val TAG = "GameContext"

    val config: GameConfig
    val gameWorld: GameWorld
    val characterTemplates: Map<Int, CharTemplate>
    val npcs: Map<Int, NpcTemplate>
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
        characterTemplates = CharTemplatesXMLReader(charsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${characterTemplates.size} player classes templates.")

        val npcsXmlFolder = File(dataFolder, NPCS_XML)
        writeInfo(TAG, "Reading npcs from ${npcsXmlFolder.absolutePath}")
        npcs = NpcXMLReader(npcsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${npcs.size} npcs.")

        writeInfo(TAG, "Initialize database")
        gameDatabase = GameDatabase(config.dataBaseConfig, characterTemplates)
    }
}