package lineage.vetal.server.game.game.model.intenttion

import lineage.vetal.server.game.game.model.behaviour.data.AttackData
import lineage.vetal.server.game.game.model.behaviour.data.TargetData
import lineage.vetal.server.game.game.model.behaviour.data.MoveData
import lineage.vetal.server.game.game.model.item.ItemObject

sealed class Intention {
    data object IDLE : Intention()
    data object ACTIVE : Intention()
    data object REST : Intention()
    data class MOVE_TO(val moveData: MoveData) : Intention()
    data object CAST : Intention()
    data class PICK(val itemObject: ItemObject) : Intention()
    data class INTERACT(val target: TargetData) : Intention()
    data class ATTACK(val data: AttackData) : Intention()
    data object FOLLOW : Intention()
    data object MOVE_TO_IN_A_BOAT : Intention()
}