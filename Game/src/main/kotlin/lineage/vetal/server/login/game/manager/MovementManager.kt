package lineage.vetal.server.login.game.manager

import lineage.vetal.server.login.game.model.location.Location
import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.gameserver.packet.server.MoveToLocation
import vetal.server.writeDebug

private const val TAG = "MovementManager"

class MovementManager {
    fun startMovement(player: Player, start: Location, finish: Location) {
        // TODO start task. Introduce map with move object ? wtf to do ? Geo engine and etc
        player.position = SpawnLocation(finish.x, finish.y, finish.z, 0)
        player.region.broadCast(MoveToLocation(player, finish))
    }

    fun startMovement(npc: Npc) {

    }

    fun onPlayerValidatePosition(player: Player, loc: Location) {
        player.position = SpawnLocation(loc)
        writeDebug(TAG, "Validate ${player.name} position $loc")
    }
}