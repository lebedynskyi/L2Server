package lineage.vetal.server.login.game

import lineage.vetal.server.core.db.HikariDBConnection
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.ConfigGame
import lineage.vetal.server.login.db.GameDatabase
import lineage.vetal.server.login.game.manager.ChatManager
import lineage.vetal.server.login.game.manager.GameAnnounceManager
import lineage.vetal.server.login.game.manager.GameLobbyManager
import lineage.vetal.server.login.game.manager.ManorManager
import lineage.vetal.server.login.game.model.GameWorld
import lineage.vetal.server.login.game.model.npc.Npc
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
    var gameAnnouncer: GameAnnounceManager
    val gameLobby: GameLobbyManager
    val gameDatabase: GameDatabase
    val gameConfig: ConfigGame
    val gameWorld: GameWorld
    val gameManor: ManorManager
    val chatManager: ChatManager

    init {
        writeSection(TAG)
        writeInfo(TAG, "Reading configs")
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")

        gameConfig = ConfigGame.read(serverConfigFile)

        val charStatsXmlFile = File(dataFolder, PATH_CLASSES_XML)
        val charStatsData = CharTemplatesXMLReader(charStatsXmlFile.absolutePath).load()
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
        gameLobby = GameLobbyManager(gameConfig, gameWorld, gameDatabase, charStatsData)
        gameManor = ManorManager()
        chatManager = ChatManager()
        gameAnnouncer = GameAnnounceManager(gameWorld).apply {
            start()
        }
    }
}