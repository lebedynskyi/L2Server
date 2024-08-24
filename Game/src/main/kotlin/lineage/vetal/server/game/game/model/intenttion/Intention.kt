package lineage.vetal.server.game.game.model.intenttion

import lineage.vetal.server.game.game.model.position.MoveData

sealed class Intention {
    data object IDLE : Intention()
    data object ACTIVE : Intention()
    data object REST : Intention()
    data class MOVE_TO(val moveData: MoveData) : Intention()
    data object CAST : Intention()
    data object PICK : Intention()
    data object ATTACK : Intention()
    data object FOLLOW : Intention()
    data object INTERACT : Intention()
    data object MOVE_TO_IN_A_BOAT : Intention()
}