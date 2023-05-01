package lineage.vetal.server.game.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.game.game.model.template.items.ItemTemplate
import lineage.vetal.server.game.game.model.template.pc.CharTemplate

class GameDatabase(
    characterTemplates: Map<Int, CharTemplate>,
    itemsTemplates: Map<Int, ItemTemplate>,
    dbConnection: DBConnection
) {
    val accountDao: AccountDao
    val charactersDao: CharactersDao
    val spawnDao: SpawnDao
    val itemsDao: ItemsDao

    init {
        charactersDao = CharactersDao(dbConnection, characterTemplates)
        itemsDao = ItemsDao(dbConnection, itemsTemplates)
        accountDao = AccountDao(dbConnection)
        spawnDao = SpawnDao(dbConnection)
    }
}