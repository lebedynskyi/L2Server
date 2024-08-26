package lineage.vetal.server.login

import lineage.vetal.server.core.bridge.BridgeClient
import lineage.vetal.server.core.model.RegisteredServer
import lineage.vetal.server.core.model.ServerStatus
import lineage.vetal.server.core.utils.logs.writeInfo
import lineage.vetal.server.login.bridgeserver.packets.server.AuthOk
import lineage.vetal.server.login.bridgeserver.packets.server.InitOK
import lineage.vetal.server.login.bridgeserver.packets.server.UpdateOk

private const val TAG = "BridgeLobby"

class BridgeLobby(
    private val registeredServers: List<RegisteredServer>
) {
    fun requestInit(client: BridgeClient, serverId: Int) {
        val server = findServer(serverId)
        if (server != null) {
            client.serverInfo = server
            client.connection.crypt.init(server.config.bridgeKey.toByteArray())
            client.sendPacket(InitOK())
        } else {
            writeInfo(TAG, "Unknown server in requestInit. Close connection")
            client.saveAndClose()
        }
    }

    fun requestAuth(client: BridgeClient, serverStatus: ServerStatus) {
        val server = findServer(serverStatus.id)
        if (server != null) {
            server.status = serverStatus
            client.sendPacket(AuthOk())
            writeInfo(TAG, "Server status updated $serverStatus")
        } else {
            writeInfo(TAG, "Unknown server in requestUpdate. Close connection")
            client.saveAndClose()
        }
    }

    fun requestUpdate(client: BridgeClient, serverStatus: ServerStatus) {
        val server = findServer(serverStatus.id)
        if (server != null) {
            server.status = serverStatus
            client.sendPacket(UpdateOk())
            writeInfo(TAG, "Server with id ${serverStatus.id} status updated $serverStatus")
        } else {
            writeInfo(TAG, "Unknown server in requestUpdate. Close connection")
            client.saveAndClose()
        }
    }

    fun onClientDisconnected(client: BridgeClient) {
        val serverId = client.serverInfo?.config?.id ?: -1
        val server = findServer(serverId)
        if (server != null) {
            server.status?.isOnline = false
            writeInfo(TAG, "Server with id $serverId disconnected")
        } else {
            writeInfo(TAG, "Unknown server with id $serverId onClientDisconnected. Nothing to do")
        }
    }

    private fun findServer(serverId: Int): RegisteredServer? {
        return registeredServers.firstOrNull { it.config.id == serverId }
    }
}