package lineage.vetal.server.game.game.model

import lineage.vetal.server.core.model.AccountInfo
import lineage.vetal.server.game.db.GameDatabase
import lineage.vetal.server.game.game.model.inventory.WearableInventory
import lineage.vetal.server.game.game.model.item.ArmorObject
import lineage.vetal.server.game.game.model.item.EtcItemObject
import lineage.vetal.server.game.game.model.item.ItemObject
import lineage.vetal.server.game.game.model.item.WeaponObject
import lineage.vetal.server.game.game.model.npc.NpcObject
import lineage.vetal.server.game.game.model.player.Appearance
import lineage.vetal.server.game.game.model.player.CharacterSex
import lineage.vetal.server.game.game.model.player.PlayerObject
import lineage.vetal.server.game.game.model.position.SpawnPosition
import lineage.vetal.server.game.game.model.template.items.ArmorItemTemplate
import lineage.vetal.server.game.game.model.template.items.EtcItemTemplate
import lineage.vetal.server.game.game.model.template.items.ItemTemplate
import lineage.vetal.server.game.game.model.template.items.WeaponItemTemplate
import lineage.vetal.server.game.game.model.template.npc.NpcTemplate
import lineage.vetal.server.game.game.model.template.pc.CharTemplate
import java.util.*

private const val OID_OFFSET = 0x10000000
private const val OID_FINISH = 0x7FFFFFFF

class GameObjectIdFactory {
    private val bitSet = BitSet(100000)
    private var lastTakenBit = 0
    private var loaded = false
    private val TAG = "ObjectIdFactory"

    fun load(gameDatabase: GameDatabase) {
        val charIds = gameDatabase.charactersDao.getAllObjectIds()
        val itemIds = gameDatabase.itemsDao.getAllObjectIds()
        charIds.plus(itemIds).forEach {
            val bit = it - OID_OFFSET
            bitSet.set(bit)
        }
        loaded = true
        lastTakenBit = bitSet.nextClearBit(0)
    }

    fun createId(): Int {
        if (!loaded) throw IllegalStateException("ObjectIdFactory not loaded")

        var freeBit = bitSet.nextClearBit(lastTakenBit)
        if (freeBit < 0) {
            freeBit = bitSet.nextClearBit(0)
        }

        if (freeBit < 0) {
            throw IllegalStateException("Out of capacity of ids set")
        }

        val newId = freeBit + OID_OFFSET

        lastTakenBit = freeBit
        bitSet.set(lastTakenBit)
        return newId
    }

    fun releaseId(id: Int) {
        val bit = id - OID_OFFSET
        if (bit < 0) {
            throw IllegalArgumentException("Invalid id detected. id $id")
        }
        bitSet.clear(bit)
    }
}

class GameObjectFactory(
    private val idFactory: GameObjectIdFactory,
    private val itemTemplates: Map<Int, ItemTemplate>,
    private val npcTemplates: Map<Int, NpcTemplate>,
    private val charTemplates: Map<Int, CharTemplate>
) {
    private val TAG = "GameObjectFactory"

    fun createPlayerObject(
        name: String, account: AccountInfo, templateId: Int,
        hairStyle: Int, hairColor: Int, face: Int, sex: Byte,
        isNewbie: Boolean
    ): PlayerObject {
        val template =
            charTemplates[templateId] ?: throw IllegalArgumentException("Cannot find char template for id $templateId")
        if (isNewbie && template.classBaseLevel > 1) {
            throw IllegalArgumentException("Cannot create newbie char for template $templateId")
        }
        val appearance = Appearance(hairStyle, hairColor, face, CharacterSex.values()[sex.toInt()])
        val position = SpawnPosition(template.spawnLocations.random())
        val objectId = idFactory.createId()
        val playerId = UUID.randomUUID().toString()
        val player = PlayerObject(objectId, playerId, account.id, appearance, name, template, position).apply {
            inventory = WearableInventory()
        }

        template.startItems.forEach {
            val item = createItemObject(it.id, it.count)
            item.ownerId = playerId
            player.inventory.addItem(item)
        }

        return player
    }

    fun createNpcObject(templateId: Int, position: SpawnPosition): NpcObject {
        val objectId = idFactory.createId()
        val template =
            npcTemplates[templateId] ?: throw IllegalArgumentException("Cannot find NPC template for id $templateId")
        return NpcObject(objectId, template, position)
    }

    fun createItemObject(templateId: Int, count: Int = 1): ItemObject {
        val objectId = idFactory.createId()
        val template =
            itemTemplates[templateId] ?: throw IllegalArgumentException("Cannot find item template for id $templateId")

        return when (template) {
            is ArmorItemTemplate -> ArmorObject(objectId, template)
            is EtcItemTemplate -> EtcItemObject(objectId, template)
            is WeaponItemTemplate -> WeaponObject(objectId, template)
            else -> throw IllegalArgumentException("Cannot fund item template of type $template")
        }.apply {
            this.count = count
        }
    }
}