package lineage.vetal.server.login.xml

import lineage.vetal.server.login.game.model.position.Position
import lineage.vetal.server.login.game.model.template.items.ItemTemplate
import lineage.vetal.server.login.game.model.template.pc.CharTemplate
import lineage.vetal.server.login.game.model.template.pc.CharSkillTemplate
import lineage.vetal.server.login.game.model.template.pc.ChatItemTemplate
import org.w3c.dom.Document
import java.nio.file.Path

class CharXMLReader(
    val path: String
) : XmlReader {
    private val TAG = "CharXMLReader"
    private val templates: MutableMap<Int, CharTemplate> = mutableMapOf()

    fun load(): Map<Int, CharTemplate> {
        parse(path)
        // We add parent skills, if existing.
        for (template in templates.values) {
            val parentClassId = template.charClass.parent
            if (parentClassId != null) {
                template.addSkillsFromParent(templates[parentClassId.id]?.skills.orEmpty())
            }
        }
        return templates
    }

    override fun parseDocument(doc: Document, path: Path) {
        forEach(doc, "list") { listNode ->
            forEach(listNode, "class") { classNode ->
                val set = StatSet()
                forEach(classNode, "set") { setNode -> set.putAll(parseAttributes(setNode)) }
                forEach(classNode, "items") { itemsNode ->
                    val items: MutableList<ChatItemTemplate> = ArrayList()
                    forEach(itemsNode, "item") { itemNode -> items.add(ChatItemTemplate(parseAttributes(itemNode))) }
                    set["items"] = items
                }
                forEach(classNode, "skills") { skillsNode ->
                    val skills: MutableList<CharSkillTemplate> = ArrayList()
                    forEach(
                        skillsNode,
                        "skill"
                    ) { skillNode -> skills.add(CharSkillTemplate(parseAttributes(skillNode))) }
                    set["skills"] = skills
                }
                forEach(classNode, "spawns") { spawnsNode ->
                    val locs: MutableList<Position> = ArrayList()
                    forEach(spawnsNode, "spawn") { spawnNode -> locs.add(Position(parseAttributes(spawnNode))) }
                    set["spawnLocations"] = locs
                }
                templates[set.getInteger("id")] = CharTemplate(set)
            }
        }
    }
}