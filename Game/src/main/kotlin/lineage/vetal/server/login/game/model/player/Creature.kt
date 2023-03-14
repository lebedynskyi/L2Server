package lineage.vetal.server.login.game.model.player

import lineage.vetal.server.login.game.model.location.SpawnLocation
import lineage.vetal.server.login.game.model.GameObject
import lineage.vetal.server.login.game.model.player.status.CreatureStatus
import lineage.vetal.server.login.gameserver.packet.server.CreatureSay
import lineage.vetal.server.login.gameserver.packet.server.MoveToLocation
import vetal.server.writeError
import java.util.*

abstract class Creature(
    id: UUID,
    name: String,
    position: SpawnLocation
) : GameObject(id, name, position) {
    private val TAG = "Creature"

    var title: String? = null
    var isRunning: Boolean = false
    var isInCombat: Boolean = false
    var isAlikeDead: Boolean = false
    var isFlying = false

    @Deprecated("should not be here.")
    open fun say(sayType: SayType, text: String) {
        if (sayType != SayType.HERO_VOICE) {
            region?.broadCast(CreatureSay(this, sayType, text), this)
        } else {
            writeError(TAG, "Broadcast packet is not implemented")
        }
    }

    @Deprecated("should not be here.")
    open fun moveToLocation(newLocation: SpawnLocation) {
        position = newLocation
        val packet = MoveToLocation(this, newLocation)
        region?.broadCast(packet)
    }

    abstract val status: CreatureStatus
}