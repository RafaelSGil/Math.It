package pt.isec.amov.mathit.model.data.multiplayer


class ServerData(var host: String, var port: Int, var name: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ServerData
        if (host == other.host && port == other.port) return true
        return false
    }

    override fun toString(): String {
        return "$name's lobby - $host:$port"
    }

    override fun hashCode(): Int {
        var result = host.hashCode()
        result = 31 * result + port
        result = 31 * result + name.hashCode()
        return result
    }
}
