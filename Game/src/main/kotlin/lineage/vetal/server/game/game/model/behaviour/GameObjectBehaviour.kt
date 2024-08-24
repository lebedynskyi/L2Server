package lineage.vetal.server.game.game.model.behaviour

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.model.intenttion.Intention

private const val TAG = "GameObjectBehaviour"

abstract class GameObjectBehaviour {
    var current: Intention = Intention.IDLE
    var next: Intention? = null

    fun setIntention(intention: Intention, nextIntention: Intention? = null) {
        current = intention
        next = nextIntention
    }

    fun setNextIntention(intention: Intention?) {
        if ((current == Intention.IDLE || current == Intention.ACTIVE) && intention != null) {
            current = intention
        } else {
            next = intention
        }
    }

    fun endCurrent(): Boolean {
        val nextIntention = next

        return if (nextIntention != null) {
            current = nextIntention
            true
        } else {
            writeDebug(TAG, "Current action is finished")
            false
        }
    }
}