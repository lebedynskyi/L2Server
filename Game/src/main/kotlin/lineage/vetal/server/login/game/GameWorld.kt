package lineage.vetal.server.login.game

class GameWorld {
    val currentOnline get() = currentPlayers.size

    private val currentPlayers = mutableSetOf<GameObject>()

    init {
        // TODO init different stuff
    }
}