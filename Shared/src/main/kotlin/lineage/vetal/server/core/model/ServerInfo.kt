package lineage.vetal.server.core.model

import lineage.vetal.server.core.ConfigRegisteredServer

class ServerInfo(
    val config: ConfigRegisteredServer,
    var status: ServerStatus? = null
)