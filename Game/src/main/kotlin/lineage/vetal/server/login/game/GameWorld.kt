package lineage.vetal.server.login.game

import lineage.vetal.server.login.game.model.player.Player

class GameWorld {
    val currentOnline get() = players.size

    private val players = mutableSetOf<Player>()
}