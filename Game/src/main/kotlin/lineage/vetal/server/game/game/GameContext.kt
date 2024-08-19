package lineage.vetal.server.game.game

import kotlinx.coroutines.Dispatchers
import lineage.vetal.server.core.db.HikariDBConnection
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.game.ConfigGame
import lineage.vetal.server.game.db.GameDatabase
import lineage.vetal.server.game.game.manager.*
import lineage.vetal.server.game.game.task.GameAnnouncerTask
import lineage.vetal.server.game.xml.CharXMLReader
import lineage.vetal.server.game.xml.ItemXMLReader
import lineage.vetal.server.game.xml.NpcXMLReader
import java.io.File

private const val PATH_SERVER_CONFIG = "game/config/Server.yaml"
private const val PATH_CLASSES_XML = "game/xml/classes"
private const val NPCS_XML = "game/xml/npcs"
private const val ITEMS_XML = "game/xml/items"
private const val TAG = "GameContext"

class GameContext {
    lateinit var worldManager: WorldManager
    lateinit var spawnManager: SpawnManager
    lateinit var movementManager: MovementManager
    lateinit var itemManager: ItemManager
    lateinit var gameAnnouncer: GameAnnouncerTask
    lateinit var gameLobby: GameLobbyManager
    lateinit var gameDatabase: GameDatabase
    lateinit var gameConfig: ConfigGame
    lateinit var manorManager: ManorManager
    lateinit var chatManager: ChatManager
    lateinit var objectFactory: GameObjectFactory

    fun load(dataFolder: String) {
        writeSection(TAG)
        writeInfo(TAG, "Loading context. Data folder is $dataFolder")

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
        dbConnection.testConnection()
        gameDatabase = GameDatabase(charTemplates, itemTemplates, dbConnection)

        val idFactory = GameObjectIdFactory()
        idFactory.load(gameDatabase)

        objectFactory = GameObjectFactory(idFactory, itemTemplates, npcTemplates, charTemplates)

        writeInfo(TAG, "Init managers")
        worldManager = WorldManager(this)
        manorManager = ManorManager()
        itemManager = ItemManager(this)
        movementManager = MovementManager(this)
        gameLobby = GameLobbyManager(this)
        chatManager = ChatManager(this)
        spawnManager = SpawnManager(this)

        val taskDispatcher = Dispatchers.IO
        writeInfo(TAG, "Init tasks")
        gameAnnouncer = GameAnnouncerTask(this, taskDispatcher)
    }
}

1

        2

        f1

        f2