package lineage.vetal.server.game.game.manager

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.server.MoveToLocation

private const val TAG = "MovementManager"

class MovementManager(
    private val context: GameContext
) {
    fun startMovement(player: PlayerObject, start: Position, finish: Position) {
//        writeDebug(TAG, "${player.name} start move $start -> $finish}, MoveSpeed ${player.stats.getMoveSpeed()}, RunSpeed ${player.stats.getBaseRunSpeed()}")
        player.position = SpawnPosition(finish.x, finish.y, finish.z, 0)
        context.worldManager.broadCast(player.region, MoveToLocation(player, finish))
    }

    fun startMovement(npc: NpcObject, finish: Position) {

    }

    fun onPlayerValidatePosition(player: PlayerObject, loc: Position) {
//        writeDebug(TAG, "${player.name} validate pos -> $loc")
        context.worldManager.onPlayerPositionChanged(player, loc)
        player.position = SpawnPosition(loc)
        // TODO save position
    }
}