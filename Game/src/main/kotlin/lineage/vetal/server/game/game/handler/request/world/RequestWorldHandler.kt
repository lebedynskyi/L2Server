package lineage.vetal.server.game.game.handler.request.world

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.handler.request.world.usecase.RestartUseCase
import lineage.vetal.server.game.game.handler.request.world.validation.RestartValidation
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.player.SayType
import lineage.vetal.server.game.game.onError
import lineage.vetal.server.game.game.onValid
import lineage.vetal.server.game.gameserver.GameClient
import lineage.vetal.server.game.gameserver.GameClientState
import lineage.vetal.server.game.gameserver.packet.server.*
import java.util.*

private const val TAG = "RequestWorldHandler"
private const val HELLO_WORLD_MSG = "Welcome in Vetalll L2 World"

class RequestWorldHandler(
    private val context: GameContext,
    private val restartValidation: RestartValidation = RestartValidation(),
    private val restartUseCase: RestartUseCase = RestartUseCase(context)
) {
    fun onRequestEnterWorld(client: GameClient, player: PlayerObject) {
        val region = context.gameWorld.getRegion(player.position.x, player.position.y)
        if (region == null) {
            writeError(TAG, "No region found for player ${player.name} and position ${player.position}")
            player.client?.close(LeaveWorld())
            return
        }

        client.clientState = GameClientState.WORLD
        client.player = player

        player.lastAccessTime = Calendar.getInstance().timeInMillis
        player.client = client
        player.isInWorld = true
        player.stats.isRunning = true
        player.region = region

        // could be done by another managers.
        player.sendPacket(UserInfo(player))
        context.requestInventoryHandler.onRequestInventoryList(player)
        player.sendPacket(QuestList())

        player.sendPacket(CreatureSay(SayType.ANNOUNCEMENT, HELLO_WORLD_MSG))

        // TODO move to function to use it here.. visiblePlayers, visibleItems etc.
        player.sendPacket(region.surround.flatMap { it.players.values }.plus(region.players.values).map { CharInfo(it) })
        player.sendPacket(region.surround.flatMap { it.npc.values }.plus(region.npc.values).map { NpcInfo(it) })
        player.sendPacket(region.surround.flatMap { it.items.values }.plus(region.items.values).map { SpawnItem(it) })

        context.broadcaster.broadCast(region, CharInfo(player))

        region.addPlayer(player)
        context.gameDatabase.charactersDao.updateLastAccess(player.objectId, player.lastAccessTime)
        writeDebug(TAG, "Player enter world. ${player.name} -> ${player.id}")
    }

    fun onRequestRestart(client: GameClient, player: PlayerObject) {
        writeDebug(TAG, "${player.name} request restart")

        restartValidation.validate(player).onValid {
            restartUseCase.onRestartSuccess(player, client)
        }.onError {
            restartUseCase.onRestartFailed(player, client, it)
        }
    }

    fun onRequestQuit(client: GameClient, player: PlayerObject) {
        writeDebug(TAG, "${player.name} request quit")

        if (!player.isInWorld) {
            client.close()
            writeError(TAG, "Not active player asked for quit. Disconnect after LeaveWorld")
        } else {
            context.gameWorld.removePlayerFromWorld(client, player)
            client.sendPacket(Logout(player.name))
            client.close(LeaveWorld())
        }
    }

    fun onPlayerDisconnected() {

    }
}