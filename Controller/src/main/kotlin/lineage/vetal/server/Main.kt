package lineage.vetal.server

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import vetal.server.sock.*
import vetal.server.writeDebug


fun main(args: Array<String>) {
    val selector: SockSelector<SockClient> = SockSelector(
        "192.168.0.137", 3456, ControllerSockClientFactory(), true, TAG = "ClientSelector"
    )
    val controller = Controller()
    val TAG = "Controller"

    runBlocking {
        launch {
            launch {
                selector.connectionAcceptFlow.collect {
                    writeDebug(TAG, "Client Connected")
                }
            }

            launch {
                selector.connectionCloseFlow.collect {
                    writeDebug(TAG, "Client disconnected")
                    controller.onClientDisconnected(it)
                }
            }

            launch {
                selector.connectionReadFlow.collect {
                    val client = it.first
                    val packet = it.second
                    writeDebug(TAG, "New packet => $packet")
                    controller.onPacketReceived(client, packet)
                }
            }
        }
        selector.start()
    }
}