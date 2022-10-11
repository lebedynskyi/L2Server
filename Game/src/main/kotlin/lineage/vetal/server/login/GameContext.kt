package lineage.vetal.server.login

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.db.GameDatabase
import lineage.vetal.server.login.game.GameWorld
import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.template.CharTemplate
import lineage.vetal.server.login.game.model.template.NpcTemplate
import lineage.vetal.server.login.xml.CharTemplatesXMLReader
import lineage.vetal.server.login.xml.NpcXMLReader
import java.io.File
import java.util.*

private const val PATH_SERVER_CONFIG = "game/config/Server.yaml"
private const val PATH_CLASSES_XML = "game/xml/classes"
private const val NPCS_XML = "game/xml/npcs"

class GameContext(
    dataFolder: String
) {
    private val TAG = "GameContext"

    val config: ConfigGame
    val gameWorld: GameWorld
    val gameDatabase: GameDatabase
    val threadPool: ThreadPool

    val charStatsData: Map<Int, CharTemplate>
    val npcsData: Map<Int, NpcTemplate>

    init {
        writeSection(TAG)
        writeInfo(TAG, "Reading configs")
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")

        config = ConfigGame.read(serverConfigFile)

        writeInfo(TAG, "Initialize thread pool")
        threadPool = ThreadPool()

        val charsXmlFolder = File(dataFolder, PATH_CLASSES_XML)
        writeInfo(TAG, "Reading classes templates from ${charsXmlFolder.absolutePath}")
        charStatsData = CharTemplatesXMLReader(charsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${charStatsData.size} player classes templates.")

        val npcsXmlFolder = File(dataFolder, NPCS_XML)
        writeInfo(TAG, "Reading npcs from ${npcsXmlFolder.absolutePath}")
        npcsData = NpcXMLReader(npcsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${npcsData.size} npcs.")

        writeInfo(TAG, "Initialize database")
        gameDatabase = GameDatabase(config.dataBaseConfig, charStatsData)

        val spawnData = gameDatabase.spawnDao.getSpawnList()
        val loadedNpc = npcsData.map {
            Npc(UUID.randomUUID(), it.value, spawnData.find { data -> data.npcTemplateId ==  it.value.idTemplate}).apply {
                objectId = it.value.id
            }
        }
        writeInfo(TAG, "Created ${loadedNpc.size} NPCs")

        gameWorld = GameWorld(loadedNpc)
    }
}