package lineage.vetal.server.login.gameserver

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import lineage.vetal.server.core.server.SelectorClientThread
import lineage.vetal.server.core.utils.logs.writeSection
import lineage.vetal.server.login.bridgeclient.BridgeGameClient
import lineage.vetal.server.login.bridgeclient.BridgeGameClientFactory
import lineage.vetal.server.login.bridgeclient.packets.client.RequestInit
import lineage.vetal.server.login.game.GameWorld
import lineage.vetal.server.login.model.GameConfig

class GameServer(
    private val gameWorld: GameWorld,
    private val config: GameConfig
) {
    private lateinit var bridgeSelector: SelectorClientThread<BridgeGameClient>

    private var bridgeClientFactory: BridgeGameClientFactory
    private var bridgeContext = newSingleThreadContext("Bridge")

    val TAG = "GameServer"

    init {
        writeSection(TAG)
        bridgeClientFactory = BridgeGameClientFactory()
    }

    suspend fun startServer() {
        bridgeSelector = SelectorClientThread(config.bridgeServer, bridgeClientFactory).apply {
            start()
        }

        withContext(bridgeContext) {
            launch {
                bridgeSelector.connectionAcceptFlow.collect {
                    it.sendPacket(RequestInit(config.serverInfo.id))
                }
            }
        }
    }
}