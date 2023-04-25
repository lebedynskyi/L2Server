package lineage.vetal.server.login.game.manager

import lineage.vetal.server.login.game.model.position.Position
import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.npc.NpcObject
import lineage.vetal.server.login.game.model.player.PlayerObject
import lineage.vetal.server.login.gameserver.packet.server.MoveToLocation

private const val TAG = "MovementManager"

class MovementManager(
    private val worldManager: WorldManager
) {
    fun startMovement(player: PlayerObject, start: Position, finish: Position) {
        // TODO start task. Introduce map with move object ? wtf to do ? Geo engine and etc
        player.position = SpawnPosition(finish.x, finish.y, finish.z, 0)
        player.region.broadCast(MoveToLocation(player, finish))
    }

    fun startMovement(npc: NpcObject) {

    }

    fun onPlayerValidatePosition(player: PlayerObject, loc: Position) {
        worldManager.onPlayerMoved(player, loc)
        player.position = SpawnPosition(loc)
    }
}