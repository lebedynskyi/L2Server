package lineage.vetal.server.login.model

import java.lang.IllegalArgumentException
import java.util.*
import java.util.stream.Stream
import kotlin.collections.HashMap

/**
 * This class is used in order to have a set of couples (key,value).<BR></BR>
 * Methods deployed are accessors to the set (add/get value from its key) and addition of a whole set in the current one.
 */
class StatsSet : HashMap<String, Any?> {
    constructor() : super() {}
    constructor(size: Int) : super(size) {}
    constructor(set: StatsSet?) : super(set) {}

    operator fun set(key: String, value: Any?) {
        put(key, value)
    }

    operator fun set(key: String, value: String?) {
        put(key, value)
    }

    operator fun set(key: String, value: Boolean) {
        put(key, if (value) java.lang.Boolean.TRUE else java.lang.Boolean.FALSE)
    }

    operator fun set(key: String, value: Int) {
        put(key, value)
    }

    operator fun set(key: String, value: IntArray?) {
        put(key, value)
    }

    operator fun set(key: String, value: Long) {
        put(key, value)
    }

    operator fun set(key: String, value: Double) {
        put(key, value)
    }

    operator fun set(key: String, value: Enum<*>?) {
        put(key, value)
    }

    fun unset(key: String) {
        remove(key)
    }

    fun getBool(key: String): Boolean {
        val value = get(key)
        if (value is Boolean) return value
        if (value is String) return java.lang.Boolean.parseBoolean(value as String?)
        if (value is Number) return value.toInt() != 0
        throw IllegalArgumentException("StatsSet : Boolean value required, but found: $value for key: $key.")
    }

    fun getBool(key: String?, defaultValue: Boolean): Boolean {
        val value = get(key)
        if (value is Boolean) return value
        if (value is String) return java.lang.Boolean.parseBoolean(value as String?)
        return if (value is Number) value.toInt() != 0 else defaultValue
    }

    fun getByte(key: String): Byte {
        val value = get(key)
        if (value is Number) return value.toByte()
        if (value is String) return value.toByte()
        throw IllegalArgumentException("StatsSet : Byte value required, but found: $value for key: $key.")
    }

    fun getByte(key: String?, defaultValue: Byte): Byte {
        val value = get(key)
        if (value is Number) return value.toByte()
        return if (value is String) value.toByte() else defaultValue
    }

    fun getDouble(key: String): Double {
        val value = get(key)
        if (value is Number) return value.toDouble()
        if (value is String) return value.toDouble()
        if (value is Boolean) return if (value) 1.0 else 0.0
        throw IllegalArgumentException("StatsSet : Double value required, but found: $value for key: $key.")
    }

    fun getDouble(key: String?, defaultValue: Double): Double {
        val value = get(key)
        if (value is Number) return value.toDouble()
        if (value is String) return value.toDouble ()
        return if (value is Boolean) if (value) 1.0 else 0.0 else defaultValue
    }

    fun getDoubleArray(key: String): DoubleArray {
        val value = get(key)
        if (value is DoubleArray) return value
        if (value is Number) return doubleArrayOf(
            value.toDouble()
        )
        if (value is String)
            return Stream.of(*value.split(";".toRegex()).toTypedArray())
                .mapToDouble { s: String -> s.toDouble() }
                .toArray()
        throw IllegalArgumentException("StatsSet : Double array required, but found: $value for key: $key.")
    }

    fun getFloat(key: String): Float {
        val value = get(key)
        if (value is Number) return value.toFloat()
        if (value is String) return value.toFloat()
        if (value is Boolean) return if (value) 1F else 0F
        throw IllegalArgumentException("StatsSet : Float value required, but found: $value for key: $key.")
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        val value = get(key)
        if (value is Number) return value.toFloat()
        if (value is String) return value.toFloat()
        return if (value is Boolean && value) if (value) 1F else 0F else defaultValue
    }

    fun getInteger(key: String): Int {
        val value = get(key)
        if (value is Number) return value.toInt()
        if (value is String) return value.toInt()
        if (value is Boolean) return if (value) 1 else 0
        throw IllegalArgumentException("StatsSet : Integer value required, but found: $value for key: $key.")
    }

    fun getInteger(key: String?, defaultValue: Int): Int {
        val value = get(key)
        if (value is Number) return value.toInt()
        if (value is String) return value.toInt()
        return if (value is Boolean) if (value) 1 else 0 else defaultValue
    }

    fun getIntegerArray(key: String): IntArray {
        val value = get(key)
        if (value is IntArray) return value
        if (value is Number) return intArrayOf(
            value.toInt()
        )
        if (value is String) return Stream.of(*value.split(";".toRegex()).toTypedArray())
            .mapToInt { s: String -> s.toInt() }
            .toArray()
        throw IllegalArgumentException("StatsSet : Integer array required, but found: $value for key: $key.")
    }

    fun getIntegerArray(key: String, defaultArray: IntArray): IntArray {
        return try {
            getIntegerArray(key)
        } catch (e: IllegalArgumentException) {
            defaultArray
        }
    }

    fun <T> getList(key: String?): List<T> {
        val value = get(key) ?: return emptyList()
        return value as List<T>
    }

    fun getLong(key: String): Long {
        val value = get(key)
        if (value is Number) return value.toLong()
        if (value is String) return value.toLong()
        if (value is Boolean) return if (value) 1L else 0L
        throw IllegalArgumentException("StatsSet : Long value required, but found: $value for key: $key.")
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        val value = get(key)
        if (value is Number) return value.toLong()
        if (value is String) return value.toLong()
        return if (value is Boolean) if (value) 1L else 0L else defaultValue
    }

    fun getLongArray(key: String): LongArray {
        val value = get(key)
        if (value is LongArray) return value
        if (value is Number) return longArrayOf(
            value.toLong()
        )
        if (value is String) return Stream.of(*value.split(";".toRegex()).toTypedArray())
            .mapToLong { s: String -> s.toLong() }
            .toArray()
        throw IllegalArgumentException("StatsSet : Long array required, but found: $value for key: $key.")
    }

    fun <T, U> getMap(key: String?): Map<T, U> {
        val value = get(key) ?: return emptyMap()
        return value as Map<T, U>
    }

    fun getString(key: String): String {
        val value = get(key)
        if (value != null) return value.toString()
        throw IllegalArgumentException("StatsSet : String value required, but unspecified for key: $key.")
    }

    fun getString(key: String?, defaultValue: String): String {
        val value = get(key)
        return value?.toString() ?: defaultValue
    }

    fun getStringArray(key: String): Array<String> {
        val value = get(key)
        if (value is Array<*>) return value as Array<String>
        if (value is String) return value.split(";".toRegex()).toTypedArray()
        throw IllegalArgumentException("StatsSet : String array required, but found: $value for key: $key.")
    }

    fun getIntIntHolder(key: String): Pair<Int, Int> {
        val value = get(key)
        if (value is Array<*>) {
            val toSplit = value as Array<String>
            return Pair(toSplit[0].toInt(), toSplit[1].toInt())
        }
        if (value is String) {
            val toSplit = value.split("-".toRegex()).toTypedArray()
            return Pair(toSplit[0].toInt(), toSplit[1].toInt())
        }
        throw IllegalArgumentException("StatsSet : int-int (IntIntHolder) required, but found: $value for key: $key.")
    }

    fun getIntIntHolderList(key: String): List<Pair<Int, Int>> {
        val value = get(key)
        if (value is String) {
            // String exists, but it empty : return a generic empty List.
            if (value.isEmpty()) return emptyList()

            // Single entry ; return the entry under List form.
            if (!value.contains(";")) {
                val toSplit = value.split("-".toRegex()).toTypedArray()
                return listOf(Pair(toSplit[0].toInt(), toSplit[1].toInt()))
            }

            // First split is using ";", second is using "-". Exemple : 1234-12;1234-12.
            val entries = value.split(";".toRegex()).toTypedArray()
            val list: MutableList<Pair<Int, Int>> = ArrayList<Pair<Int, Int>>(entries.size)

            // Feed the List.
            for (entry in entries) {
                val toSplit = entry.split("-".toRegex()).toTypedArray()
                list.add(Pair(toSplit[0].toInt(), toSplit[1].toInt()))
            }
            return list
        }
        throw IllegalArgumentException("StatsSet : int-int;int-int (List<IntIntHolder>) required, but found: $value for key: $key.")
    }

    fun getIntIntHolderList(key: String, defaultHolder: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        return try {
            getIntIntHolderList(key)
        } catch (e: IllegalArgumentException) {
            defaultHolder
        }
    }

    fun <A> getObject(key: String?, type: Class<A>): A? {
        val value = get(key)
        return if (value == null || !type.isAssignableFrom(value.javaClass)) null else value as A
    }

    fun <E : Enum<E>?> getEnum(name: String?, enumClass: Class<E>): E {
        val value = get(name)
        if (value != null && enumClass.isInstance(value)) return value as E
        if (value is String) return java.lang.Enum.valueOf(enumClass, value as String?)
        throw IllegalArgumentException("Enum value of type " + enumClass.name + "required, but found: " + value + ".")
    }

    fun <E : Enum<E>?> getEnum(name: String?, enumClass: Class<E>, defaultValue: E): E {
        val value = get(name)
        if (value != null && enumClass.isInstance(value)) return value as E
        return if (value is String) java.lang.Enum.valueOf(enumClass, value as String?) else defaultValue
    }
}