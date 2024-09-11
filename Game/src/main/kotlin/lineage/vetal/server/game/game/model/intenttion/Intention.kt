package lineage.vetal.server.game.game.model.intenttion

import lineage.vetal.server.game.game.model.behaviour.data.AttackData
import lineage.vetal.server.game.game.model.behaviour.data.TargetData
import lineage.vetal.server.game.game.model.behaviour.data.MoveData
import lineage.vetal.server.game.game.model.item.ItemObject

sealed class Intention {
    data object IDLE : Intention()
    data object REST : Intention()
    data class MOVE_TO(val data: MoveData) : Intention()
    data object CAST : Intention()
    data class PICK(val data: ItemObject) : Intention()
    data class INTERACT(val data: TargetData) : Intention()
    data class ATTACK(val data: AttackData) : Intention()
    data object FOLLOW : Intention()
}