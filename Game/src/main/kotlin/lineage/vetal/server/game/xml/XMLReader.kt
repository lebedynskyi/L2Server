package lineage.vetal.server.game.xml

import lineage.vetal.server.core.utils.logs.writeError
import lineage.vetal.server.game.game.model.position.Position
import lineage.vetal.server.game.game.model.position.SpawnPosition
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import javax.xml.parsers.DocumentBuilderFactory

private const val TAG = "XmlReader"

// TODO redesign parser. Make it return data without holding it inside.
interface XmlReader {
    fun parseDocument(doc: Document, path: Path)
    fun parse(path: String) {
        parse(Paths.get(path), validate = false, ignoreComments = true, ignoreWhitespaces = true)
    }

    fun parse(path: Path, validate: Boolean, ignoreComments: Boolean, ignoreWhitespaces: Boolean) {
        if (Files.isDirectory(path)) {
            val pathsToParse: MutableList<Path> = LinkedList()
            try {
                Files.walkFileTree(
                    path,
                    EnumSet.noneOf(FileVisitOption::class.java),
                    Int.MAX_VALUE,
                    object : SimpleFileVisitor<Path>() {
                        override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                            pathsToParse.add(file)
                            return FileVisitResult.CONTINUE
                        }
                    })
                pathsToParse.forEach(Consumer { p: Path ->
                    parse(
                        p,
                        validate,
                        ignoreComments,
                        ignoreWhitespaces
                    )
                })
            } catch (e: IOException) {
                writeError(TAG, "Could not parse file: $path", e)
            }
        } else {
            val dbf = DocumentBuilderFactory.newInstance()
            dbf.isNamespaceAware = true
            dbf.isValidating = validate
            dbf.isIgnoringComments = ignoreComments
            dbf.isIgnoringElementContentWhitespace = ignoreWhitespaces
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA)
            try {
                val db = dbf.newDocumentBuilder()
                db.setErrorHandler(XMLErrorHandler())
                parseDocument(db.parse(path.toAbsolutePath().toFile()), path)
            } catch (e: SAXParseException) {
                writeError(TAG, "Could not parse file: $path at line: ${e.lineNumber}, column: ${e.columnNumber}", e)
            } catch (e: Exception) {
                writeError(TAG, "Could not parse file: $path", e)
            }
        }
    }

    fun parseBoolean(node: Node?, defaultValue: Boolean? = null): Boolean? {
        return if (node != null) java.lang.Boolean.valueOf(node.nodeValue) else defaultValue
    }

    fun parseBoolean(attrs: NamedNodeMap, name: String?): Boolean? {
        return parseBoolean(attrs.getNamedItem(name))
    }

    fun parseBoolean(attrs: NamedNodeMap, name: String?, defaultValue: Boolean?): Boolean? {
        return parseBoolean(attrs.getNamedItem(name), defaultValue)
    }

    fun parseByte(node: Node?, defaultValue: Byte? = null): Byte? {
        return if (node != null) java.lang.Byte.decode(node.nodeValue) else defaultValue
    }

    fun parseByte(attrs: NamedNodeMap, name: String?): Byte? {
        return parseByte(attrs.getNamedItem(name))
    }

    fun parseByte(attrs: NamedNodeMap, name: String?, defaultValue: Byte?): Byte? {
        return parseByte(attrs.getNamedItem(name), defaultValue)
    }

    fun parseShort(node: Node?, defaultValue: Short? = null): Short? {
        return if (node != null) java.lang.Short.decode(node.nodeValue) else defaultValue
    }

    fun parseShort(attrs: NamedNodeMap, name: String?): Short? {
        return parseShort(attrs.getNamedItem(name))
    }

    fun parseShort(attrs: NamedNodeMap, name: String?, defaultValue: Short?): Short? {
        return parseShort(attrs.getNamedItem(name), defaultValue)
    }

    fun parseInt(node: Node?, defaultValue: Int? = -1): Int? {
        return if (node != null) Integer.decode(node.nodeValue) else defaultValue
    }

    fun parseInteger(node: Node?, defaultValue: Int? = null): Int? {
        return if (node != null) Integer.decode(node.nodeValue) else defaultValue
    }

    fun parseInteger(attrs: NamedNodeMap, name: String?): Int? {
        return parseInteger(attrs.getNamedItem(name))
    }

    fun parseInteger(attrs: NamedNodeMap, name: String?, defaultValue: Int?): Int? {
        return parseInteger(attrs.getNamedItem(name), defaultValue)
    }

    fun parseLong(node: Node?, defaultValue: Long? = null): Long? {
        return if (node != null) java.lang.Long.decode(node.nodeValue) else defaultValue
    }

    fun parseLong(attrs: NamedNodeMap, name: String?): Long? {
        return parseLong(attrs.getNamedItem(name))
    }

    fun parseLong(attrs: NamedNodeMap, name: String?, defaultValue: Long?): Long? {
        return parseLong(attrs.getNamedItem(name), defaultValue)
    }

    fun parseFloat(node: Node?, defaultValue: Float? = null): Float? {
        return if (node != null) java.lang.Float.valueOf(node.nodeValue) else defaultValue
    }

    fun parseFloat(attrs: NamedNodeMap, name: String?): Float? {
        return parseFloat(attrs.getNamedItem(name))
    }

    fun parseFloat(attrs: NamedNodeMap, name: String?, defaultValue: Float?): Float? {
        return parseFloat(attrs.getNamedItem(name), defaultValue)
    }

    fun parseDouble(node: Node?, defaultValue: Double? = null): Double? {
        return if (node != null) java.lang.Double.valueOf(node.nodeValue) else defaultValue
    }

    fun parseDouble(attrs: NamedNodeMap, name: String?): Double? {
        return parseDouble(attrs.getNamedItem(name))
    }

    fun parseDouble(attrs: NamedNodeMap, name: String?, defaultValue: Double?): Double? {
        return parseDouble(attrs.getNamedItem(name), defaultValue)
    }

    fun parseString(node: Node?, defaultValue: String? = null): String? {
        return if (node != null) node.nodeValue else defaultValue
    }

    fun parseString(attrs: NamedNodeMap, name: String?): String? {
        return parseString(attrs.getNamedItem(name))
    }

    fun parseString(attrs: NamedNodeMap, name: String?, defaultValue: String?): String? {
        return parseString(attrs.getNamedItem(name), defaultValue)
    }

    fun <T : Enum<T>?> parseEnum(node: Node?, clazz: Class<T>, defaultValue: T): T {
        return if (node == null) {
            defaultValue
        } else try {
            java.lang.Enum.valueOf(clazz, node.nodeValue)
        } catch (e: IllegalArgumentException) {
            writeError(
                TAG,
                "Invalid value specified for node: ${node.nodeName} specified value: ${node.nodeValue}, should be enum value of \"${clazz.simpleName}\" using default value: $defaultValue",
                e
            )
            defaultValue
        }
    }

    fun parseAttributes(node: Node): StatSet {
        val attrs = node.attributes
        val map = StatSet()
        for (i in 0 until attrs.length) {
            val att = attrs.item(i)
            map.put(att.nodeName, att.nodeValue)
        }
        return map
    }

    fun addAttributes(set: StatSet, attrs: NamedNodeMap) {
        for (i in 0 until attrs.length) {
            val att = attrs.item(i)
            set.put(att.nodeName, att.nodeValue)
        }
    }

    fun parseLocation(n: Node): Position {
        val attrs = n.attributes
        val x = parseInteger(attrs, "x") ?: 0
        val y = parseInteger(attrs, "y") ?: 0
        val z = parseInteger(attrs, "z") ?: 0
        return Position(x, y, z)
    }

    fun parseSpawnLocation(n: Node): SpawnPosition {
        val attrs = n.attributes
        val x = parseInteger(attrs, "x") ?: 0
        val y = parseInteger(attrs, "y") ?: 0
        val z = parseInteger(attrs, "z") ?: 0
        val heading = parseInteger(attrs, "heading") ?: 0
        return SpawnPosition(x, y, z, heading)
    }

    fun forEach(node: Node, action: Consumer<Node>) {
        forEach(node, { a: Node -> true }, action)
    }

    fun forEach(node: Node, nodeName: String, action: Consumer<Node>) {
        forEach(node, { innerNode: Node ->
            if (nodeName.contains("|")) {
                val nodeNames = nodeName.split("\\|".toRegex()).toTypedArray()
                for (name in nodeNames) {
                    if (name.isNotEmpty() && name == innerNode.nodeName) {
                        return@forEach true
                    }
                }
                return@forEach false
            }
            nodeName == innerNode.nodeName
        }, action)
    }

    fun forEach(node: Node, filter: Predicate<Node>, action: Consumer<Node>) {
        val list = node.childNodes
        for (i in 0 until list.length) {
            val targetNode = list.item(i)
            if (filter.test(targetNode)) {
                action.accept(targetNode)
            }
        }
    }

    class XMLErrorHandler : ErrorHandler {
        @Throws(SAXParseException::class)
        override fun warning(e: SAXParseException) {
            throw e
        }

        @Throws(SAXParseException::class)
        override fun error(e: SAXParseException) {
            throw e
        }

        @Throws(SAXParseException::class)
        override fun fatalError(e: SAXParseException) {
            throw e
        }
    }

    companion object {
        fun isNode(node: Node): Boolean {
            return node.nodeType == Node.ELEMENT_NODE
        }

        fun isText(node: Node): Boolean {
            return node.nodeType == Node.TEXT_NODE
        }

        const val JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage"
        const val W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema"
    }
}