package lineage.vetal.server.login.bridgeclient

import lineage.vetal.server.core.client.Client
import lineage.vetal.server.core.client.ClientConnection

class BridgeGameClient(
    override val connection: ClientConnection
) : Client()