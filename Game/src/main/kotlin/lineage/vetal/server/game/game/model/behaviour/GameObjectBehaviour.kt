package lineage.vetal.server.game.game.model.behaviour

import lineage.vetal.server.core.utils.logs.writeDebug
import lineage.vetal.server.game.game.model.intenttion.Intention

private const val TAG = "GameObjectBehaviour"

abstract class GameObjectBehaviour {
    var action: Intention = Intention.IDLE; private set
    var intention: Intention? = null; private set

    open fun setAction(action: Intention, intention: Intention? = null) {
        this.action = action
        this.intention = intention
    }

    open fun setIntention(intention: Intention?) {
        if ((action == Intention.IDLE) && intention != null) {
            this.action = intention
        } else {
            this.intention = intention
        }
    }

    open fun endCurrent(): Boolean {
        writeDebug(TAG, "Finished $action")
        val nextIntention = intention

        return if (nextIntention != null) {
            action = nextIntention
            intention = null
            writeDebug(TAG, "Started Next $nextIntention")
            true
        } else {
            false
        }
    }
}