package lineage.vetal.server.login.game

import lineage.vetal.server.core.server.SendablePacket
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.gameclient.packet.server.CharInfo
import lineage.vetal.server.login.gameclient.packet.server.DeleteObject
import lineage.vetal.server.login.gameclient.packet.server.UserInfo
import javax.swing.text.Position

// TODO use blocks and regions in game. For now it is ok to use global world
class GameWorld {
    val currentOnline get() = _players.size
    val players: List<Player> get() = _players
    private val _players = mutableListOf<Player>()
    private val _objects = mutableListOf<GameObject>()

    fun addObject(obj: GameObject) {
        if (obj is Player) {
            spawnPlayer(obj)
            _players.add(obj)
        } else {
            _objects.add(obj)
        }
    }

    fun removeObject(obj: GameObject) {
        if (obj is Player) {
            _players.remove(obj)
        } else {
            _objects.remove(obj)
        }
        broadCastPacket(DeleteObject(obj))
    }

    fun broadCastPacket(packet: SendablePacket, range: Int = 0) {
        _players.forEach {
            it.sendPacket(packet)
        }
    }

    private fun spawnPlayer(player: Player) {
        for (p in players) {
            p.sendPacket(CharInfo(player))
            player.sendPacket(CharInfo(p))
        }
    }
}