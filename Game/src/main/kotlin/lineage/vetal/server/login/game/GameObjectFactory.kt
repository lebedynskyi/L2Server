package lineage.vetal.server.login.game

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.login.game.model.item.ArmorObject
import lineage.vetal.server.login.game.model.item.EtcItemObject
import lineage.vetal.server.login.game.model.item.ItemObject
import lineage.vetal.server.login.game.model.item.WeaponObject
import lineage.vetal.server.login.game.model.npc.NpcObject
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
    private val itemTemplates: Map<Int, ItemTemplate>,
    private val npcTemplates: Map<Int, NpcTemplate>
) {
    @Deprecated("Template should be removed from here!!!")
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

    fun createItemObject(templateId: Int, playerId: String, count: Int = 1): ItemObject {
        val objectId = idFactory.createId()
        val template = itemTemplates[templateId] ?: throw IllegalArgumentException("Cannot find item template for id $templateId")

        return when (template) {
            is ArmorItemTemplate -> ArmorObject(objectId, playerId, template)
            is EtcItemTemplate -> EtcItemObject(objectId, playerId, template)
            is WeaponItemTemplate -> WeaponObject(objectId, playerId, template)
            else -> throw IllegalArgumentException("Cannot fund item template of type $template")
        }.apply {
            this.count = count
        }
    }

    fun createNpcObject(templateId: Int, position: SpawnPosition): NpcObject {
        val objectId = idFactory.createId()
        val template = npcTemplates[templateId]?: throw IllegalArgumentException("Cannot find NPC template for id $templateId")
        return NpcObject(objectId, template, position)
    }
}