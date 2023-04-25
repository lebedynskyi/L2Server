package lineage.vetal.server.login.db.sql

class ItemsSQL {
    companion object {
        const val INSERT_OR_UPDATE_ITEM_SQL = "" +
                "INSERT INTO `items` (owner_id, object_id, item_id, count, enchant_level, item_location, item_location_data, custom_type1, custom_type2, duration_left, create_time)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)"
        const val SELECT_ITEMS_FOR_PLAYER_SQL = "" +
                "SELECT owner_id, object_id, item_id, count, enchant_level, item_location, item_location_data, custom_type1, custom_type2, duration_left, create_time " +
                "FROM `items` " +
                "WHERE owner_id=?"
        const val SELECT_ITEMS_IDS_SQL = "" +
                "SELECT object_id " +
                "FROM `items` "
    }
}