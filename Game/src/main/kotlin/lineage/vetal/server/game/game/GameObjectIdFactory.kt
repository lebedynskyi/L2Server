package lineage.vetal.server.game.game

import lineage.vetal.server.game.db.GameDatabase
import java.util.*

// All id below is reserved by NPC
private const val OID_OFFSET = 0x10000000
private const val OID_FINISH = 0x7FFFFFFF
private const val TAG = "ObjectIdFactory"

class GameObjectIdFactory {

    private val bitSet = BitSet(100000)
    private var lastTakenBit = 0
    private var loaded = false

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
            throw  IllegalArgumentException("Invalid id detected. id $id")
        }
        bitSet.clear(bit)
    }
}