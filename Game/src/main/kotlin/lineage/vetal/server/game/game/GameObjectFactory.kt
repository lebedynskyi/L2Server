package lineage.vetal.server.game.game

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.game.game.model.item.*
import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.game.model.player.Appearance
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.player.CharacterSex
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.template.items.ArmorItemTemplate
import lineage.vetal.server.game.game.model.template.items.EtcItemTemplate
import lineage.vetal.server.game.game.model.template.items.ItemTemplate
import lineage.vetal.server.game.game.model.template.items.WeaponItemTemplate
import lineage.vetal.server.game.game.model.template.npc.NpcTemplate
import lineage.vetal.server.game.game.model.template.pc.CharTemplate
import java.util.UUID
import kotlin.IllegalArgumentException

class GameObjectFactory(
    private val idFactory: GameObjectIdFactory,
    private val itemTemplates: Map<Int, ItemTemplate>,
    private val npcTemplates: Map<Int, NpcTemplate>,
    private val charTemplates: Map<Int, CharTemplate>
) {
    fun createPlayerObject(
        name: String, account: AccountInfo, templateId: Int,
        hairStyle: Int, hairColor: Int, face: Int, sex: Byte,
        isNewbie: Boolean
    ): PlayerObject {
        val template = charTemplates[templateId] ?: throw IllegalArgumentException("Cannot find char template for id $templateId")
        if (isNewbie && template.classBaseLevel > 1) {
            throw IllegalArgumentException("Cannot create newbie char for template $templateId")
        }
        val appearance = Appearance(hairStyle, hairColor, face, CharacterSex.values()[sex.toInt()])
        val position = SpawnPosition(template.spawnLocations.random())
        val objectId = idFactory.createId()
        val playerId = UUID.randomUUID().toString()
        val player = PlayerObject(playerId, account.id, objectId, name, template, appearance, position)

        template.startItems.forEach {
            val item = createItemObject(it.id, playerId, it.count)
            player.inventory.addItem(item)
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