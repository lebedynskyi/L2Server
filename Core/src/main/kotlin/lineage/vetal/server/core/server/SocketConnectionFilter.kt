package lineage.vetal.server.core.server

open class SocketConnectionFilter(
    protected val listOfBannedIp: List<String>
) {
    open fun acceptConnection(ip: String) = listOfBannedIp.contains(ip)
}