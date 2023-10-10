package lineage.vetal.server.game.game.manager

import lineage.vetal.server.game.game.GameContext
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.gameserver.packet.server.MoveToLocation
import java.util.Calendar

private const val TAG = "MovementManager"

class MovementManager(
    private val context: GameContext
) {
    fun startMovement(player: PlayerObject, start: Position, finish: Position) {
        System.err.println("${Calendar.getInstance().time}: Start move $start -> $finish}")
        System.err.println("${Calendar.getInstance().time}: Start move MoveSpeed ${player.stats.getMoveSpeed()}, RunSpeed ${player.stats.getBaseRunSpeed()}")
        player.position = SpawnPosition(finish.x, finish.y, finish.z, 0)
        player.region.broadCast(MoveToLocation(player, finish))
    }

    fun startMovement(npc: NpcObject, finish: Position) {
        npc.region.broadCast(MoveToLocation(npc, finish))
    }

    fun onPlayerValidatePosition(player: PlayerObject, loc: Position) {
        System.err.println("${Calendar.getInstance().time}: Validate -> $loc")
        context.worldManager.onPlayerPositionChanged(player, loc)
        player.position = SpawnPosition(loc)
    }
}