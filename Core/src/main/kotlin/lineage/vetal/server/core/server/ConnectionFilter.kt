package lineage.vetal.server.core.server

class ConnectionFilter(
    private val listOfBannedIp: List<String>
) {
    fun acceptConnection(ip: String) = listOfBannedIp.contains(ip)
}