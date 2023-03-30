package lineage.vetal.server.login.game

import lineage.vetal.server.core.db.HikariDBConnection
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.ConfigGame
import lineage.vetal.server.login.db.GameDatabase
import lineage.vetal.server.login.game.manager.*
import lineage.vetal.server.login.xml.CharXMLReader
import lineage.vetal.server.login.xml.ItemXMLReader
import lineage.vetal.server.login.xml.NpcXMLReader
import java.io.File

private const val PATH_SERVER_CONFIG = "game/config/Server.yaml"
private const val PATH_CLASSES_XML = "game/xml/classes"
private const val NPCS_XML = "game/xml/npcs"
private const val ITEMS_XML = "game/xml/items"
private const val TAG = "GameContext"

class GameContext(
    dataFolder: String,
) {
    val worldManager: WorldManager
    val movementManager:MovementManager
    val gameAnnouncer: GameAnnounceManager
    val gameLobby: GameLobbyManager
    val gameDatabase: GameDatabase
    val gameConfig: ConfigGame
    val manorManager: ManorManager
    val chatManager: ChatManager
    val objectFactory: ObjectFactory

    init {
        writeSection(TAG)
        writeInfo(TAG, "Reading configs")
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")

        gameConfig = ConfigGame.read(serverConfigFile)

        val itemsXmlFolder = File(dataFolder, ITEMS_XML)
        val itemsData = ItemXMLReader(itemsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${itemsData.size} items.")

        val charStatsXmlFile = File(dataFolder, PATH_CLASSES_XML)
        val charStatsData = CharXMLReader(charStatsXmlFile.absolutePath).load()
        writeInfo(TAG, "Loaded ${charStatsData.size} player classes templates.")

        val npcsXmlFolder = File(dataFolder, NPCS_XML)
        val npcsData = NpcXMLReader(npcsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${npcsData.size} npcs.")

        writeInfo(TAG, "Initialize database")
        gameDatabase = GameDatabase(charStatsData, HikariDBConnection(gameConfig.dataBaseConfig))
        val npcsSpawnData = gameDatabase.spawnDao.getSpawnList()
        val idFactory = ObjectIdFactory()
        idFactory.load(gameDatabase)
        objectFactory = ObjectFactory(idFactory, npcsSpawnData)

        // TODO move it to spawn manager
        val loadedNpc = npcsData.map {
            objectFactory.createNpc(it.value.id, it.value)
        }

        writeInfo(TAG, "Start managers")
        worldManager = WorldManager(loadedNpc, gameDatabase)
        manorManager = ManorManager()
        movementManager = MovementManager(worldManager)
        gameLobby = GameLobbyManager(gameConfig, worldManager, gameDatabase, objectFactory, charStatsData)
        chatManager = ChatManager(worldManager)
        gameAnnouncer = GameAnnounceManager(chatManager).apply {
            start()
        }
    }
}