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
    val spawnManager: SpawnManager
    val movementManager: MovementManager
    val itemManager: ItemManager
    val gameAnnouncer: GameAnnounceManager
    val gameLobby: GameLobbyManager
    val gameDatabase: GameDatabase
    val gameConfig: ConfigGame
    val manorManager: ManorManager
    val chatManager: ChatManager
    val objectFactory: GameObjectFactory

    init {
        writeSection(TAG)
        writeInfo(TAG, "Reading configs")
        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")

        gameConfig = ConfigGame.read(serverConfigFile)

        val itemsXmlFolder = File(dataFolder, ITEMS_XML)
        val itemTemplates = ItemXMLReader(itemsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${itemTemplates.size} items.")

        val charsXmlFolder = File(dataFolder, PATH_CLASSES_XML)
        val charTemplates = CharXMLReader(charsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${charTemplates.size} player classes templates.")

        val npcXmlFolder = File(dataFolder, NPCS_XML)
        val npcTemplates = NpcXMLReader(npcXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${npcTemplates.size} npcs.")

        writeInfo(TAG, "Initialize database")
        val dbConnection = HikariDBConnection(gameConfig.dataBaseConfig)
        gameDatabase = GameDatabase(charTemplates, itemTemplates, dbConnection)

        val idFactory = GameObjectIdFactory()
        idFactory.load(gameDatabase)

        objectFactory = GameObjectFactory(idFactory, itemTemplates, npcTemplates, charTemplates)

        writeInfo(TAG, "Start managers")
        worldManager = WorldManager(gameDatabase)
        manorManager = ManorManager()
        itemManager = ItemManager(worldManager)
        movementManager = MovementManager(worldManager)
        gameLobby = GameLobbyManager(gameConfig, worldManager, gameDatabase, objectFactory, charTemplates)
        chatManager = ChatManager(worldManager)
        spawnManager = SpawnManager(worldManager, gameDatabase, objectFactory)
        gameAnnouncer = GameAnnounceManager(chatManager).apply {
            start()
        }
        writeInfo(TAG,"Managers started")
    }
}