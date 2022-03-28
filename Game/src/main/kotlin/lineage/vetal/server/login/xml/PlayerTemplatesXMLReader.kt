package lineage.vetal.server.login.xml

import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.model.ClassId
import lineage.vetal.server.login.model.Location
import lineage.vetal.server.login.model.template.ItemTemplate
import lineage.vetal.server.login.model.template.PlayerTemplate
import lineage.vetal.server.login.model.template.SkillTemplate
import org.w3c.dom.Document
import java.nio.file.Path

class PlayerTemplatesXMLReader(
    val path: String
) : XmlReader {
    private val TAG = "CharTemplatesXMLReader"
    private val templates: MutableMap<Int, PlayerTemplate> = mutableMapOf()

    fun load(): MutableMap<Int, PlayerTemplate> {
        loadXml()
        return templates
    }

    private fun loadXml() {
        parse(path)
        writeInfo(TAG, "Loaded ${templates.size} player classes templates.")

        // We add parent skills, if existing.
        for (template in templates.values) {
            val parentClassId = template.classId.parent
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
                templates[set.getInteger("id")] = PlayerTemplate(set)
            }
        }
    }

    fun getTemplate(classId: ClassId): PlayerTemplate? {
        return templates[classId.id]
    }

    fun getTemplate(classId: Int): PlayerTemplate? {
        return templates[classId]
    }

    fun getClassNameById(classId: Int): String {
        val template: PlayerTemplate? = templates[classId]
        return template?.classId?.name ?: "Invalid class"
    }
}