package lineage.vetal.server.game.game.utils

import lineage.vetal.server.game.game.model.position.Position
import kotlin.math.atan2
import kotlin.math.sqrt

// 65536 / 360 - converts degrees into the client's 16 bit heading space
private const val HEADINGS_IN_DEGREE = 182.04444444444444

object MathUtils {
    /**
     * Calculates the client heading (0..65535) pointing from [from] towards [to].
     */
    fun calculateHeadingFrom(from: Position, to: Position): Int {
        var angle = Math.toDegrees(atan2((to.y - from.y).toDouble(), (to.x - from.x).toDouble()))
        if (angle < 0) {
            angle += 360.0
        }
        return (angle * HEADINGS_IN_DEGREE).toInt()
    }

    fun isWithinRadius(position: Position, destination: Position, radius: Double): Boolean {
        // Calculate the Euclidean distance between position and destination
        val distance = sqrt(
            ((position.x - destination.x).toDouble() * (position.x - destination.x)) +
                    ((position.y - destination.y).toDouble() * (position.y - destination.y)) +
                    ((position.z - destination.z).toDouble() * (position.z - destination.z))
        )

        // Check if the distance is within the radius
        return distance <= radius
    }

    fun hasExceededDestination(current: Position, destination: Position, newPosition: Position): Boolean {
        return (newPosition.x > destination.x && current.x <= destination.x) ||
                (newPosition.y > destination.y && current.y <= destination.y) ||
                (newPosition.z > destination.z && current.z <= destination.z) ||
                (newPosition.x < destination.x && current.x >= destination.x) ||
                (newPosition.y < destination.y && current.y >= destination.y) ||
                (newPosition.z < destination.z && current.z >= destination.z)
    }
}