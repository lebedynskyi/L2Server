package lineage.vetal.server.game.game.manager

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
        // TODO start task. Introduce map with move object ? wtf to do ? Geo engine and etc
        player.position = SpawnPosition(finish.x, finish.y, finish.z, 0)
        player.region.broadCast(MoveToLocation(player, finish))
    }

    fun startMovement(npc: NpcObject, finish: Position) {
        npc.region.broadCast(MoveToLocation(npc, finish))
    }

    fun onPlayerValidatePosition(player: PlayerObject, loc: Position) {
        context.worldManager.onPlayerPositionChanged(player, loc)
        player.position = SpawnPosition(loc)
    }
}