package lineage.vetal.server.core.server

open class SocketConnectionFilter(
    private val listOfBannedIp: List<String>
) {
    open fun acceptConnection(ip: String) = listOfBannedIp.contains(ip)
}