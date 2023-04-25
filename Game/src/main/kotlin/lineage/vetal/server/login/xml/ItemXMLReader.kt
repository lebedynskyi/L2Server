package lineage.vetal.server.login.xml

import lineage.vetal.server.login.game.model.template.items.ItemTemplate
import org.w3c.dom.Document
import vetal.server.writeError
import java.nio.file.Path

class ItemXMLReader(
    val path: String
) : XmlReader{
    private val TAG = "ItemXMLReader"
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
                set["id"] = itemId
                set["name"] = parseString(attrs, "name")
                set["type"] = parseString(attrs, "type")
                set["title"] = parseString(attrs, "title")

                forEach(itemNode, "set") { setNode ->
                    val setAttrs = setNode.attributes
                    val key = parseString(setAttrs, "name")
                    if (key != null) {
                        set[key] = parseString(setAttrs, "val")
                    }
                }

                if (itemId != null) {
                    templates[itemId] = ItemTemplate(set)
                } else {
                    writeError(TAG, "Unable to parse item. No id found. Name ${set.getString("name")}")
                }
            }
        }
    }
}