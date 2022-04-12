package lineage.vetal.server.login.game.model.npc

import lineage.vetal.server.login.game.model.location.SpawnLocation

data class SpawnData(
    val npcTemplateId: Int,
    val spawnLocation: SpawnLocation,
    val respawnDelay: Int,
    val respawnRandom: Int,
    val periodOfDay: Int
)