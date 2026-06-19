package lineage.vetal.server.game.game.model.behaviour.data

import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.utils.MathUtils
import kotlin.math.sqrt

/**
 * Holds an immutable snapshot of a single "move to" order.
 *
 * The position is interpolated from a fixed [origin] using the total elapsed time since
 * [moveStartTime] (see BehaviourMoveToUseCase). Because every tick recomputes the absolute
 * position from this fixed origin, sub-unit rounding never accumulates into drift.
 */
data class MoveData(
    val origin: Position,
    val destination: Position,
    val moveStartTime: Long,
) {
    val heading: Int = MathUtils.calculateHeadingFrom(origin, destination)

    /**
     * Total distance of the move. Uses the geodata tolerance rule: when horizontally close but
     * vertically far apart, the vertical component is ignored so client/server geodata mismatches
     * don't stall the move. Should not be applied to vertical movement in water or during flight.
     */
    val totalDistance: Double = run {
        val dx = (destination.x - origin.x).toDouble()
        val dy = (destination.y - origin.y).toDouble()
        val dz = (destination.z - origin.z).toDouble()
        val delta2d = dx * dx + dy * dy
        if (delta2d < 10000 && (dz * dz > 2500)) sqrt(delta2d) else sqrt(delta2d + dz * dz)
    }
}
