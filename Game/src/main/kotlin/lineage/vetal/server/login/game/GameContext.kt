package lineage.vetal.server.login.game

import lineage.vetal.server.core.db.HikariDBConnection
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.ConfigGame
import lineage.vetal.server.login.db.GameDatabase
import lineage.vetal.server.login.game.manager.GameAnnounceManager
import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.template.CharTemplate
import lineage.vetal.server.login.xml.CharTemplatesXMLReader
import lineage.vetal.server.login.xml.NpcXMLReader
import java.io.File
import java.util.*

private const val PATH_SERVER_CONFIG = "game/config/Server.yaml"
private const val PATH_CLASSES_XML = "game/xml/classes"
private const val NPCS_XML = "game/xml/npcs"
private const val TAG = "GameContext"

class GameContext(
    dataFolder: String,

) {
    val gameDatabase: GameDatabase
    val gameConfig: ConfigGame
    val gameLobby: GameLobby
    val gameWorld: GameWorld
    var gameAnnouncer: GameAnnounceManager
    val charStatsData: MutableMap<Int, CharTemplate>

    init {
        writeSection(TAG)
        writeInfo(TAG, "Reading configs")
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")

        gameConfig = ConfigGame.read(serverConfigFile)

        val charsXmlFolder = File(dataFolder, PATH_CLASSES_XML)
        charStatsData = CharTemplatesXMLReader(charsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${charStatsData.size} player classes templates.")

        val npcsXmlFolder = File(dataFolder, NPCS_XML)
        val npcsData = NpcXMLReader(npcsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${npcsData.size} npcs.")

        writeInfo(TAG, "Initialize database")
        gameDatabase = GameDatabase(charStatsData, HikariDBConnection(gameConfig.dataBaseConfig))

        writeInfo(TAG, "Start managers")
        val spawnData = gameDatabase.spawnDao.getSpawnList()
        val loadedNpc = npcsData.map {
            Npc(UUID.randomUUID(), it.value, spawnData.find { data -> data.npcTemplateId ==  it.value.idTemplate}).apply {
                objectId = it.value.id
            }
        }
        gameWorld = GameWorld(loadedNpc)
        gameLobby = GameLobby(gameConfig, gameWorld)
        gameAnnouncer = GameAnnounceManager(gameWorld).apply {
            start()
        }
    }
}