package pt.isec.amov.mathit.utils

import org.json.JSONObject
import pt.isec.amov.mathit.model.data.ServerData

fun serverDataToJson(server: ServerData): JSONObject {
    val jsonObject = JSONObject().also {
        it.put("host", server.host)
        it.put("port", server.port)
        it.put("name", server.name)
    }
    return jsonObject
}

fun jsonObjectToServerData(jsonObject: JSONObject): ServerData {
    val name = jsonObject.getString("name")
    val host = jsonObject.getString("host")
    val port = jsonObject.getInt("port")
    return ServerData(host, port, name)
}

