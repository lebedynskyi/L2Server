package lineage.vetal.server.login.game

import java.util.UUID

open class GameObject(
    val id: UUID,
    var name: String,
) {
    var objectId: Int = 0
    var isVisible = false
}