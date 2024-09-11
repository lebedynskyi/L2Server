package lineage.vetal.server.game.game

import kotlinx.coroutines.Dispatchers
import lineage.vetal.server.core.db.HikariDBConnection
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.game.ConfigGameServer
import lineage.vetal.server.game.db.GameDatabase
import lineage.vetal.server.game.game.handler.request.action.RequestActionHandler
import lineage.vetal.server.game.game.handler.request.chat.RequestChatHandler
import lineage.vetal.server.game.game.handler.request.item.RequestItemHandler
import lineage.vetal.server.game.game.handler.request.auth.RequestAuthHandler
import lineage.vetal.server.game.game.handler.request.movement.RequestMovementHandler
import lineage.vetal.server.game.game.handler.request.world.RequestWorldHandler
import lineage.vetal.server.game.game.manager.AnnounceManager
import lineage.vetal.server.game.game.manager.GameWorldManager
import lineage.vetal.server.game.game.manager.InteractManager
import lineage.vetal.server.game.game.manager.behaviour.attack.AttackManager
import lineage.vetal.server.game.game.manager.manor.ManorManager
import lineage.vetal.server.game.game.manager.behaviour.movement.MovementManager
import lineage.vetal.server.game.game.manager.spawn.SpawnManager
import lineage.vetal.server.game.game.task.TickTaskManager
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

private val DEFAULT_ANNOUNCE_TICK_PERIOD = TimeUnit.MINUTES.toMillis(1)
private val DEFAULT_ATTACK_TICK_PERIOD = TimeUnit.MILLISECONDS.toMillis(100)
private val DEFAULT_MOVEMENT_TICK_PERIOD = TimeUnit.MILLISECONDS.toMillis(100)

private val defaultAnnouncements = listOf(
    "Welcome on Vitalii Lebedynskyi server",
    "This is the best Vetalll server",
    "I did an announce manager!",
    "Some random message"
)

class GameContext {
    // Data
    lateinit var gameConfig: ConfigGameServer
    lateinit var gameDatabase: GameDatabase

    // Managers
    lateinit var gameWorld: GameWorldManager
    lateinit var spawnManager: SpawnManager
    lateinit var manorManager: ManorManager
    lateinit var movementManager: MovementManager
    lateinit var attackManager: AttackManager
    lateinit var announceManager: AnnounceManager
    lateinit var interactionManager: InteractManager

    // Handlers
    lateinit var requestItemHandler: RequestItemHandler
    lateinit var requestActionHandler: RequestActionHandler
    lateinit var requestAuthHandler: RequestAuthHandler
    lateinit var requestWorldHandler: RequestWorldHandler
    lateinit var requestChatHandler: RequestChatHandler
    lateinit var requestMovementHandler: RequestMovementHandler

    // Misc
    lateinit var clock: Clock
    lateinit var objectFactory: GameObjectFactory

    // Tasks
    lateinit var tickTaskManager: TickTaskManager

    fun load(dataFolder: String) {
        writeSection(TAG)
        writeInfo(TAG, "Loading context. Data folder is $dataFolder")

        val serverConfigFile = File(dataFolder, PATH_SERVER_CONFIG)
        writeInfo(TAG, "Reading game server configs from ${serverConfigFile.absolutePath}")
        gameConfig = ConfigGameServer.read(serverConfigFile)
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

        writeSection("Handlers")
        requestItemHandler = RequestItemHandler(this)
        requestAuthHandler = RequestAuthHandler(this)
        requestWorldHandler = RequestWorldHandler(this)
        requestChatHandler = RequestChatHandler(this)
        requestActionHandler = RequestActionHandler(this)
        requestMovementHandler = RequestMovementHandler(this)

        writeSection("Managers")
        gameWorld = GameWorldManager(this)
        manorManager = ManorManager(this)
        spawnManager = SpawnManager(this)
        movementManager = MovementManager(this)
        attackManager = AttackManager(this)
        announceManager = AnnounceManager(requestChatHandler, defaultAnnouncements)
        interactionManager = InteractManager(this)

        writeSection("Tasks")
        val taskDispatcher = Dispatchers.IO
        tickTaskManager = TickTaskManager(clock, taskDispatcher).apply {
            register(announceManager, DEFAULT_ANNOUNCE_TICK_PERIOD)
            register(movementManager, DEFAULT_MOVEMENT_TICK_PERIOD)
            register(attackManager, DEFAULT_ATTACK_TICK_PERIOD)
        }
    }
}
