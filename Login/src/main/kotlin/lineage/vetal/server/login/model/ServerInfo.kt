package lineage.vetal.server.login.model

import lineage.vetal.server.core.ConfigRegisteredServer
import lineage.vetal.server.core.model.ServerStatus

class ServerInfo(
    val config: ConfigRegisteredServer,
    var status: ServerStatus? = null
)