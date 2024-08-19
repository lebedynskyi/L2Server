package lineage.vetal.server.game.db

import lineage.vetal.server.core.db.DBConnection
import lineage.vetal.server.game.game.model.template.items.ItemTemplate
import lineage.vetal.server.game.game.model.template.pc.CharTemplate

class GameDatabase(
    characterTemplates: Map<Int, CharTemplate>,
    itemsTemplates: Map<Int, ItemTemplate>,
    dbConnection: DBConnection
) {
    val accountDao: AccountDao = AccountDao(dbConnection)
    val charactersDao: CharactersDao = CharactersDao(dbConnection, characterTemplates)
    val spawnDao: SpawnDao = SpawnDao(dbConnection)
    val itemsDao: ItemsDao = ItemsDao(dbConnection, itemsTemplates)
}