package lineage.vetal.server.game.game.utils

import lineage.vetal.server.game.game.model.position.Position
import kotlin.math.sqrt

object MathUtils {
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