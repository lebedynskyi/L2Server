package lineage.vetal.server.login.game

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.login.game.model.item.ArmorObject
import lineage.vetal.server.login.game.model.item.EtcItemObject
import lineage.vetal.server.login.game.model.item.ItemObject
import lineage.vetal.server.login.game.model.item.WeaponObject
import lineage.vetal.server.login.game.model.npc.NpcObject
import lineage.vetal.server.login.game.model.npc.NpcSpawnData
import lineage.vetal.server.login.game.model.player.Appearance
import lineage.vetal.server.login.game.model.player.PlayerObject
import lineage.vetal.server.login.game.model.player.CharacterSex
import lineage.vetal.server.login.game.model.position.SpawnPosition
import lineage.vetal.server.login.game.model.template.items.ArmorItemTemplate
import lineage.vetal.server.login.game.model.template.items.EtcItemTemplate
import lineage.vetal.server.login.game.model.template.items.ItemTemplate
import lineage.vetal.server.login.game.model.template.items.WeaponItemTemplate
import lineage.vetal.server.login.game.model.template.npc.NpcTemplate
import lineage.vetal.server.login.game.model.template.pc.CharTemplate
import java.util.UUID
import kotlin.IllegalArgumentException

class GameObjectFactory(
    private val idFactory: GameObjectIdFactory,
    private val npcSpawnData: List<NpcSpawnData>,
    private val itemData: Map<Int, ItemTemplate>,
    private val npcData: Map<Int, NpcTemplate>
) {
    fun createPlayerObject(
        name: String, account: AccountInfo, charTemplate: CharTemplate,
        hairStyle: Int, hairColor: Int, face: Int, sex: Byte
    ): PlayerObject {
        // TODO create inventory
        val appearance = Appearance(hairStyle, hairColor, face, CharacterSex.values()[sex.toInt()])
        val position = SpawnPosition(charTemplate.spawnLocations.random())
        val objectId = idFactory.createId()
        val playerId = UUID.randomUUID().toString()
        val player = PlayerObject(playerId, account.id, objectId, name, charTemplate, appearance, position)

        charTemplate.startItems.forEach {
            player.inventory.addItem(createItemObject(it.id, playerId, it.count))
        }

        return player
    }

    fun createItemObject(id: Int, playerId: String, count: Int = 1): ItemObject {
        val objectId = idFactory.createId()
        val template = itemData[id] ?: throw IllegalArgumentException("Cannot find item template for id $id")

        return when (template) {
            is ArmorItemTemplate -> ArmorObject(objectId, playerId, template)
            is EtcItemTemplate -> EtcItemObject(objectId, playerId, template)
            is WeaponItemTemplate -> WeaponObject(objectId, playerId, template)
            else -> throw IllegalArgumentException("Cannot fund item template of type $template")
        }.apply {
            this.count = count
        }
    }

    fun createNpcObject(id: Int, template: NpcTemplate): NpcObject {
        val spawnPoint = npcSpawnData.find { data -> data.npcTemplateId == template.idTemplate }
        return NpcObject(id, template, spawnPoint)
    }
}