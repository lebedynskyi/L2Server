package lineage.vetal.server.game.game

import kotlinx.coroutines.Dispatchers
import lineage.vetal.server.core.db.HikariDBConnection
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.game.ConfigGame
import lineage.vetal.server.game.db.GameDatabase
import lineage.vetal.server.game.game.manager.*
import lineage.vetal.server.game.game.manager.item.ItemManager
import lineage.vetal.server.game.game.manager.movement.MovementManager
import lineage.vetal.server.game.game.task.ScheduleTaskManager
import lineage.vetal.server.game.game.task.TickTaskManager
import lineage.vetal.server.game.game.task.tick.DeleteItemOnGroundTickTask
import lineage.vetal.server.game.game.task.tick.GameAnnouncerTask
import lineage.vetal.server.game.game.task.tick.MovementTickTask
import lineage.vetal.server.game.xml.CharXMLReader
import lineage.vetal.server.game.xml.ItemXMLReader
import lineage.vetal.server.game.xml.NpcXMLReader
import java.io.File
import java.time.Clock
import java.util.concurrent.TimeUnit

private const val PATH_SERVER_CONFIG = "game/config/Server.yaml"
private const val PATH_CLASSES_XML = "game/xml/classes"
private const val NPCS_XML = "game/xml/npcs"
private const val ITEMS_XML = "game/xml/items"
private const val TAG = "GameContext"

private val DEFAULT_ANNOUNCE_PERIOD = TimeUnit.MINUTES.toMillis(3)
private val DEFAULT_DELETE_ITEMS_PERIOD = TimeUnit.MINUTES.toMillis(1)
private val DEFAULT_MOVEMENT_PERIOD = TimeUnit.MILLISECONDS.toMillis(100)

private val defaultAnnouncements = listOf(
    "Welcome on Vitalii Lebedynskyi server",
    "This is the best Vetalll server",
    "I did an announce manager!",
    "Some random message"
)

class GameContext {
    lateinit var worldManager: WorldManager
    lateinit var spawnManager: SpawnManager
    lateinit var movementManager: MovementManager
    lateinit var itemManager: ItemManager
    lateinit var gameLobby: GameLobbyManager
    lateinit var gameDatabase: GameDatabase
    lateinit var gameConfig: ConfigGame
    lateinit var manorManager: ManorManager
    lateinit var chatManager: ChatManager
    lateinit var objectFactory: GameObjectFactory

    lateinit var clock: Clock
    lateinit var scheduleTaskManager: ScheduleTaskManager
    lateinit var tickTaskManager: TickTaskManager

    private lateinit var announceTask: GameAnnouncerTask
    private lateinit var movementTask: MovementTickTask
    private lateinit var deleteItemsTask: DeleteItemOnGroundTickTask

    fun load(dataFolder: String) {
        writeSection(TAG)
        writeInfo(TAG, "Loading context. Data folder is $dataFolder")

        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")
        gameConfig = ConfigGame.read(serverConfigFile)
        clock = Clock.systemUTC()

        val itemsXmlFolder = File(dataFolder, ITEMS_XML)
        val itemTemplates = ItemXMLReader(itemsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${itemTemplates.size} items.")

        val charsXmlFolder = File(dataFolder, PATH_CLASSES_XML)
        val charTemplates = CharXMLReader(charsXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${charTemplates.size} player classes templates.")

        val npcXmlFolder = File(dataFolder, NPCS_XML)
        val npcTemplates = NpcXMLReader(npcXmlFolder.absolutePath).load()
        writeInfo(TAG, "Loaded ${npcTemplates.size} npcs.")

        writeSection("Database")
        val dbConnection = HikariDBConnection(gameConfig.dataBaseConfig)
        dbConnection.testConnection()
        gameDatabase = GameDatabase(charTemplates, itemTemplates, dbConnection)

        val idFactory = GameObjectIdFactory()
        idFactory.load(gameDatabase)

        objectFactory = GameObjectFactory(idFactory, itemTemplates, npcTemplates, charTemplates)

        writeSection("Managers")
        worldManager = WorldManager(this)
        manorManager = ManorManager(this)
        itemManager = ItemManager(this)
        movementManager = MovementManager(this)
        gameLobby = GameLobbyManager(this)
        chatManager = ChatManager(this)
        spawnManager = SpawnManager(this)

        writeSection("Tasks")
        announceTask = GameAnnouncerTask(chatManager, defaultAnnouncements)
        movementTask = MovementTickTask(movementManager)
        deleteItemsTask = DeleteItemOnGroundTickTask(itemManager)

        val taskDispatcher = Dispatchers.IO
        scheduleTaskManager = ScheduleTaskManager(clock, taskDispatcher)
        tickTaskManager = TickTaskManager(clock, taskDispatcher)

        tickTaskManager.register(announceTask, DEFAULT_ANNOUNCE_PERIOD, DEFAULT_ANNOUNCE_PERIOD)
        tickTaskManager.register(movementTask, DEFAULT_MOVEMENT_PERIOD, DEFAULT_MOVEMENT_PERIOD)
        tickTaskManager.register(deleteItemsTask, DEFAULT_DELETE_ITEMS_PERIOD, DEFAULT_DELETE_ITEMS_PERIOD)
    }
}
