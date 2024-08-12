package lineage.vetal.server.game.xml

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.game.game.model.template.items.ArmorItemTemplate
import lineage.vetal.server.game.game.model.template.items.EtcItemTemplate
import lineage.vetal.server.game.game.model.template.items.ItemTemplate
import lineage.vetal.server.game.game.model.template.items.WeaponItemTemplate
import org.w3c.dom.Document
import java.nio.file.Path

private const val TAG = "ItemXMLReader"

class ItemXMLReader(
    val path: String
) : XmlReader {
    private val templates: MutableMap<Int, ItemTemplate> = mutableMapOf()

    fun load(): Map<Int, ItemTemplate> {
        parse(path)
        return templates
    }

    override fun parseDocument(doc: Document, path: Path) {
        forEach(doc, "list") { listNode ->
            forEach(listNode, "item") { itemNode ->
                val set = StatSet()
                val attrs = itemNode.attributes
                val itemId = parseInteger(attrs, "id")
                val itemType = parseString(attrs, "type")

                if (itemId != null) {
                    set["id"] = itemId
                    set["name"] = parseString(attrs, "name")
                    set["title"] = parseString(attrs, "title")

                    forEach(itemNode, "set") { setNode ->
                        val setAttrs = setNode.attributes
                        val key = parseString(setAttrs, "name")
                        if (key != null) {
                            set[key] = parseString(setAttrs, "val")
                        }
                    }

                    templates[itemId] = when (itemType) {
                        "Weapon" -> WeaponItemTemplate(set)
                        "Armor" -> ArmorItemTemplate(set)
                        else -> EtcItemTemplate(set)
                    }
                } else {
                    writeInfo(TAG, "Unable to parse item. No id found. Name ${set.getString("name")}")
                }
            }
        }
    }
}