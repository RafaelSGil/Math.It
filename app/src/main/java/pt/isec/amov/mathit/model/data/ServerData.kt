package pt.isec.amov.mathit.model.data


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
}
