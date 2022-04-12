package lineage.vetal.server.login.xml

import lineage.vetal.server.login.game.model.template.NpcTemplate
import lineage.vetal.server.login.game.model.template.PetTemplate
import org.w3c.dom.Document
import java.nio.file.Path

class NpcXMLReader(
    val path: String
) : XmlReader {
    private val TAG = "NpcXMLReader"
    private val npcs: MutableMap<Int, NpcTemplate> = mutableMapOf()

    fun load(): Map<Int, NpcTemplate> {
        parse(path)
        return npcs
    }

    override fun parseDocument(doc: Document, path: Path) {
        forEach(doc, "list") { listNode ->
            forEach(listNode, "npc") { npcNode ->
                val attrs = npcNode.attributes
                val npcId = parseInteger(attrs, "id")
                val templateId =
                    if (attrs.getNamedItem("idTemplate") == null) npcId else parseInteger(attrs, "idTemplate")
                val set = StatSet()
                set["id"] = npcId
                set["idTemplate"] = templateId
                set["name"] = parseString(attrs, "name")
                set["title"] = parseString(attrs, "title")
                forEach(npcNode, "set") { setNode ->
                    val setAttrs = setNode.attributes
                    val key = parseString(setAttrs, "name")
                    if (key != null) {
                        set[key] = parseString(setAttrs, "val")
                    }
                }
                forEach(npcNode, "ai") { aiNode ->
                    val aiAttrs = aiNode.attributes
                    set["aiType"] = parseString(aiAttrs, "type")
                    set["ssCount"] = parseInteger(aiAttrs, "ssCount")
                    set["ssRate"] = parseInteger(aiAttrs, "ssRate")
                    set["spsCount"] = parseInteger(aiAttrs, "spsCount")
                    set["spsRate"] = parseInteger(aiAttrs, "spsRate")
                    set["aggro"] = parseInteger(aiAttrs, "aggro")
                    if (aiAttrs.getNamedItem("clan") != null) {
                        set["clan"] = parseString(aiAttrs, "clan")!!.split(";")
                        set["clanRange"] = parseInteger(aiAttrs, "clanRange")
                        if (aiAttrs.getNamedItem("ignoredIds") != null) set.set(
                            "ignoredIds",
                            parseString(aiAttrs, "ignoredIds")
                        )
                    }
                    set["canMove"] = parseBoolean(aiAttrs, "canMove")
                    set["seedable"] = parseBoolean(aiAttrs, "seedable")
                }
//                forEach(npcNode, "drops") { dropsNode ->
//                    val type: String = set.getString("type")
//                    val isRaid = type.equals("RaidBoss", ignoreCase = true) || type.equals("GrandBoss", ignoreCase = true)
//                    val drops: MutableList<DropCategory> = ArrayList<DropCategory>()
//                    forEach(dropsNode, "category") { categoryNode ->
//                        val categoryAttrs = categoryNode.attributes
//                        val category = DropCategory(parseInteger(categoryAttrs, "id"))
//                        forEach(categoryNode, "drop") { dropNode ->
//                            val dropAttrs = dropNode.attributes
//                            val data = DropData(
//                                parseInteger(dropAttrs, "itemid"),
//                                parseInteger(dropAttrs, "min"),
//                                parseInteger(dropAttrs, "max"),
//                                parseInteger(dropAttrs, "chance")
//                            )
//                            if (ItemData.getInstance().getTemplate(data.getItemId()) == null) {
//                                LOGGER.warn("Droplist data for undefined itemId: {}.", data.getItemId())
//                                return@forEach
//                            }
//                            category.addDropData(data, isRaid)
//                        }
//                        drops.add(category)
//                    }
//                    set.set("drops", drops)
//                }
//                forEach(npcNode, "minions") { minionsNode ->
//                    val minions: MutableList<MinionData> = ArrayList<MinionData>()
//                    forEach(minionsNode, "minion") { minionNode ->
//                        val minionAttrs = minionNode.attributes
//                        val data = MinionData()
//                        data.setMinionId(parseInteger(minionAttrs, "id"))
//                        data.setAmountMin(parseInteger(minionAttrs, "min"))
//                        data.setAmountMax(parseInteger(minionAttrs, "max"))
//                        minions.add(data)
//                    }
//                    set.set("minions", minions)
//                }
//                forEach(npcNode, "petdata") { petdataNode ->
//                    val petdataAttrs = petdataNode.attributes
//                    set["mustUsePetTemplate"] = true
//                    set["food1"] = parseInteger(petdataAttrs, "food1")
//                    set["food2"] = parseInteger(petdataAttrs, "food2")
//                    set["autoFeedLimit"] = parseDouble(petdataAttrs, "autoFeedLimit")
//                    set["hungryLimit"] = parseDouble(petdataAttrs, "hungryLimit")
//                    set["unsummonLimit"] = parseDouble(petdataAttrs, "unsummonLimit")
//                    val entries: MutableMap<Int, PetDataEntry> = HashMap<Int, PetDataEntry>()
//                    forEach(petdataNode, "stat") { statNode ->
//                        val petSet: StatSet = parseAttributes(statNode)
//                        entries[petSet.getInteger("level")] = PetDataEntry(petSet)
//                    }
//                    set["petData"] = entries
//                }
//                forEach(npcNode, "skills") { skillsNode ->
//                    val skills: MutableList<L2Skill> = ArrayList<L2Skill>()
//                    forEach(skillsNode, "skill") { skillNode ->
//                        val skillAttrs = skillNode.attributes
//                        val skillId = parseInteger(skillAttrs, "id")
//                        val level = parseInteger(skillAttrs, "level")
//                        if (skillId == L2Skill.SKILL_NPC_RACE) {
//                            set["raceId"] = level
//                            return@forEach
//                        }
//                        val skill: L2Skill = SkillTable.getInstance().getInfo(skillId, level) ?: return@forEach
//                        skills.add(skill)
//                    }
//                    set.set("skills", skills)
//                }
//                forEach(npcNode, "teachTo") { teachToNode ->
//                    set.set(
//                        "teachTo",
//                        parseString(teachToNode.attributes, "classes")
//                    )
//                }
                if (npcId != null) {
                    npcs[npcId] = if (set.getBool("mustUsePetTemplate", false)){
                        PetTemplate(set)
                    }else {
                        NpcTemplate(set)
                    }
                }
            }
        }
    }
}