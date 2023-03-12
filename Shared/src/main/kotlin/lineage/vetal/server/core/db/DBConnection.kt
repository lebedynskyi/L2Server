package lineage.vetal.server.core.db

import java.sql.Connection

interface DBConnection {
    val connection: Connection
}