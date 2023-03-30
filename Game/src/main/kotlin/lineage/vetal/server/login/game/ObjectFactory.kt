package lineage.vetal.server.login.game

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.login.game.model.npc.Npc
import lineage.vetal.server.login.game.model.npc.NpcSpawnData
import lineage.vetal.server.login.game.model.player.Appearance
import lineage.vetal.server.login.game.model.player.Player
import lineage.vetal.server.login.game.model.player.CharacterSex
import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.template.npc.NpcTemplate
import lineage.vetal.server.login.game.model.template.pc.CharTemplate

class ObjectFactory(
    private val idFactory: ObjectIdFactory,
    private val npcsSpawnData: List<NpcSpawnData>
) {
    fun createPlayer(
        name: String,
        account: AccountInfo,
        charTemplate: CharTemplate,
        hairStyle: Int,
        hairColor: Int,
        face: Int,
        sex: Byte
    ): Player {
        val appearance = Appearance(hairStyle, hairColor, face, CharacterSex.values()[sex.toInt()])
        val position = SpawnPosition(charTemplate.spawnLocations.random())
        val playerId = idFactory.createId()
        return Player(playerId, name, account.id, charTemplate, appearance, position)
    }

    fun createItem() {

    }

    fun createNpc(id: Int, template: NpcTemplate): Npc {
        val spawnPoint = npcsSpawnData.find { data -> data.npcTemplateId == template.idTemplate }
        return Npc(id, template, spawnPoint)
    }
}