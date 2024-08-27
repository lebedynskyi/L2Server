package lineage.vetal.server.game.game

import kotlinx.coroutines.Dispatchers
import lineage.vetal.server.core.db.HikariDBConnection
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.game.ConfigGame
import lineage.vetal.server.game.db.GameDatabase
import lineage.vetal.server.game.game.handler.request.action.RequestActionHandler
import lineage.vetal.server.game.game.handler.tick.behaviour.BehaviourManager
import lineage.vetal.server.game.game.handler.request.chat.RequestChatHandler
import lineage.vetal.server.game.game.handler.request.item.RequestItemHandler
import lineage.vetal.server.game.game.handler.tick.GameLobbyManager
import lineage.vetal.server.game.game.handler.tick.manor.ManorManager
import lineage.vetal.server.game.game.handler.tick.spawn.SpawnManager
import lineage.vetal.server.game.game.task.ScheduleTaskManager
import lineage.vetal.server.game.game.task.TickTaskManager
import lineage.vetal.server.game.game.task.tick.DeleteItemOnGroundTickTask
import lineage.vetal.server.game.game.task.tick.GameAnnouncerTask
import lineage.vetal.server.game.game.task.tick.BehaviourTask
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

private val DEFAULT_ANNOUNCE_TICK_PERIOD = TimeUnit.MINUTES.toMillis(3)
private val DEFAULT_DELETE_ITEMS_TICK_PERIOD = TimeUnit.MINUTES.toMillis(1)
private val DEFAULT_BEHAVIOUR_TICK_PERIOD = TimeUnit.MILLISECONDS.toMillis(100)

private val defaultAnnouncements = listOf(
    "Welcome on Vitalii Lebedynskyi server",
    "This is the best Vetalll server",
    "I did an announce manager!",
    "Some random message"
)

class GameContext {
    lateinit var gameWorld: GameWorld
    lateinit var spawnManager: SpawnManager
    lateinit var requestItemHandler: RequestItemHandler
    lateinit var requestActionHandler: RequestActionHandler
    lateinit var gameLobby: GameLobbyManager
    lateinit var gameDatabase: GameDatabase
    lateinit var gameConfig: ConfigGame
    lateinit var manorManager: ManorManager
    lateinit var requestChatHandler: RequestChatHandler
    lateinit var behaviourManager: BehaviourManager
    lateinit var objectFactory: GameObjectFactory

    lateinit var clock: Clock
    lateinit var scheduleTaskManager: ScheduleTaskManager
    lateinit var tickTaskManager: TickTaskManager

    private lateinit var announceTask: GameAnnouncerTask
    private lateinit var behaviourTask: BehaviourTask
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
        gameWorld = GameWorld(this)
        manorManager = ManorManager(this)
        requestItemHandler = RequestItemHandler(this)
        gameLobby = GameLobbyManager(this)
        requestChatHandler = RequestChatHandler(this)
        spawnManager = SpawnManager(this)
        behaviourManager = BehaviourManager(this)
        requestActionHandler = RequestActionHandler(this)

        writeSection("Tasks")
        announceTask = GameAnnouncerTask(requestChatHandler, defaultAnnouncements)
        behaviourTask = BehaviourTask(behaviourManager)
        deleteItemsTask = DeleteItemOnGroundTickTask(requestItemHandler)

        val taskDispatcher = Dispatchers.IO
        scheduleTaskManager = ScheduleTaskManager(clock, taskDispatcher)
        tickTaskManager = TickTaskManager(clock, taskDispatcher)

        tickTaskManager.register(announceTask, DEFAULT_ANNOUNCE_TICK_PERIOD, DEFAULT_ANNOUNCE_TICK_PERIOD)
        tickTaskManager.register(behaviourTask, DEFAULT_BEHAVIOUR_TICK_PERIOD, DEFAULT_BEHAVIOUR_TICK_PERIOD)
        tickTaskManager.register(deleteItemsTask, DEFAULT_DELETE_ITEMS_TICK_PERIOD, DEFAULT_DELETE_ITEMS_TICK_PERIOD)
    }
}
