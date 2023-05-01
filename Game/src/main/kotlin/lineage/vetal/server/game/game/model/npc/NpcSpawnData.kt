package lineage.vetal.server.game.game.model.npc

import lineage.vetal.server.game.game.model.position.SpawnPosition

data class NpcSpawnData(
    val npcTemplateId: Int,
    val spawnPosition: SpawnPosition,
    val respawnDelay: Int,
    val respawnRandom: Int,
    val periodOfDay: Int
)