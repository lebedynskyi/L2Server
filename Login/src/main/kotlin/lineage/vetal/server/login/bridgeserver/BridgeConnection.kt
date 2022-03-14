package lineage.vetal.server.login.bridgeserver

import lineage.vetal.server.core.client.ClientConnection
import lineage.vetal.server.core.server.ReceivablePacket
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel

class BridgeConnection(
    socket: SocketChannel,
    selectionKey: SelectionKey,
    clientAddress: InetSocketAddress
) : ClientConnection(socket, selectionKey, clientAddress) {

    override fun readData(byteBuffer: ByteBuffer, stringBuffer: StringBuffer): ReceivablePacket? {
        TODO("Not yet implemented")
    }

    override fun writeData(byteBuffer: ByteBuffer, tempBuffer: ByteBuffer) {
        TODO("Not yet implemented")
    }
}