package lineage.vetal.server.login.game

import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.login.game.model.player.Player

class GameWorld {
    val currentOnline get() = players.size
    private val players = mutableListOf<Player>()
    private val objects = mutableListOf<GameObject>()

    fun addObject(obj: GameObject) {
        if (obj is Player) {
            players.add(obj)
        } else {
            objects.add(obj)
        }
    }

    fun removeObject(obj: GameObject) {
        if (obj is Player) {
            players.remove(obj)
        } else {
            objects.remove(obj)
        }
    }

    fun broadCastPacket(packet: SendablePacket, range: Int = 0) {
        players.forEach {
            it.sendPacket(packet)
        }
    }
}