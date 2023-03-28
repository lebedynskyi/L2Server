package lineage.vetal.server.login.game.manager

import lineage.vetal.server.login.game.model.position.Position
import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.gameserver.packet.server.MoveToLocation

private const val TAG = "MovementManager"

class MovementManager(
    private val worldManager: WorldManager
) {
    fun startMovement(player: Player, start: Position, finish: Position) {
        // TODO start task. Introduce map with move object ? wtf to do ? Geo engine and etc
        player.position = SpawnPosition(finish.x, finish.y, finish.z, 0)
        player.region.broadCast(MoveToLocation(player, finish))
    }

    fun startMovement(npc: Npc) {

    }

    fun onPlayerValidatePosition(player: Player, loc: Position) {
        worldManager.onPlayerMoved(player, loc)
        player.position = SpawnPosition(loc)
    }
}