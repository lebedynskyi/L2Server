package lineage.vetal.server.login.model

import lineage.vetal.server.core.ConfigRegisteredServer
import lineage.vetal.server.core.model.ServerStatus

class ConnectedServer(
    private val serverInfo: ConfigRegisteredServer,
    private val serverStatus: ServerStatus
)