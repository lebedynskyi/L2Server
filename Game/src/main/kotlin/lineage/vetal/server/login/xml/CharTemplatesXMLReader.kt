package lineage.vetal.server.login.xml

import lineage.vetal.server.core.model.location.Location
import lineage.vetal.server.login.game.model.template.ItemTemplate
import lineage.vetal.server.login.game.model.template.CharTemplate
import lineage.vetal.server.login.game.model.template.SkillTemplate
import org.w3c.dom.Document
import java.nio.file.Path

class CharTemplatesXMLReader(
    val path: String
) : XmlReader {
    private val TAG = "CharTemplatesXMLReader"
    private val templates: MutableMap<Int, CharTemplate> = mutableMapOf()

    fun load(): MutableMap<Int, CharTemplate> {
        loadXml()
        return templates
    }

    private fun loadXml() {
        parse(path)

        // We add parent skills, if existing.
        for (template in templates.values) {
            val parentClassId = template.charClass.parent
            if (parentClassId != null) {
                template.addSkillsFromParent(templates[parentClassId.id]?.skills.orEmpty())
            }
        }
    }

    override fun parseDocument(doc: Document, path: Path) {
        forEach(doc, "list") { listNode ->
            forEach(listNode, "class") { classNode ->
                val set = StatSet()
                forEach(classNode, "set") { setNode -> set.putAll(parseAttributes(setNode)) }
                forEach(classNode, "items") { itemsNode ->
                    val items: MutableList<ItemTemplate> = ArrayList()
                    forEach(itemsNode, "item") { itemNode -> items.add(ItemTemplate(parseAttributes(itemNode))) }
                    set["items"] = items
                }
                forEach(classNode, "skills") { skillsNode ->
                    val skills: MutableList<SkillTemplate> = ArrayList()
                    forEach(
                        skillsNode,
                        "skill"
                    ) { skillNode -> skills.add(SkillTemplate(parseAttributes(skillNode))) }
                    set["skills"] = skills
                }
                forEach(classNode, "spawns") { spawnsNode ->
                    val locs: MutableList<Location> = ArrayList()
                    forEach(spawnsNode, "spawn") { spawnNode -> locs.add(Location(parseAttributes(spawnNode))) }
                    set["spawnLocations"] = locs
                }
                templates[set.getInteger("id")] = CharTemplate(set)
            }
        }
    }
}