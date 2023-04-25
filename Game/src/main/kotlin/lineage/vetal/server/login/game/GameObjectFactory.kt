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
import lineage.vetal.server.login.game.model.template.items.ItemTemplate
import lineage.vetal.server.login.game.model.template.items.ItemType
import lineage.vetal.server.login.game.model.template.npc.NpcTemplate
import lineage.vetal.server.login.game.model.template.pc.CharTemplate
import java.lang.IllegalArgumentException
import java.util.UUID

class GameObjectFactory(
    private val idFactory: ObjectIdFactory,
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
            player.inventory.addItem(createItemObject(it.id, it.count))
        }

        return player
    }

    fun createItemObject(id: Int, count: Int = 1): ItemObject {
        val objectId = idFactory.createId()
        val template = itemData[id] ?: throw IllegalArgumentException("Cannot find item template for id $id")

        return when (template.itemType) {
            ItemType.Armor -> ArmorObject(objectId, template)
            ItemType.EtcItem -> EtcItemObject(objectId, template)
            ItemType.Weapon -> WeaponObject(objectId, template)
        }
    }

    fun createNpcObject(id: Int, template: NpcTemplate): NpcObject {
        val spawnPoint = npcSpawnData.find { data -> data.npcTemplateId == template.idTemplate }
        return NpcObject(id, template, spawnPoint)
    }
}