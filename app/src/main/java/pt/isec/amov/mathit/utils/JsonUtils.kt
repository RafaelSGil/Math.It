package pt.isec.amov.mathit.utils

import com.google.android.material.shape.ShapePath.PathLineOperation
import org.json.JSONObject
import pt.isec.amov.mathit.model.data.Player
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

fun playerToJson(player: Player) : JSONObject {
    val jsonObject = JSONObject().also {
        it.put("player_name", player.name)
        it.put("score", player.score)
    }
    return jsonObject
}

fun jsonObjectToPlayer(jsonObject: JSONObject) : Player {
    val name = jsonObject.getString("player_name")
    val score = jsonObject.getLong("score")
    val player = Player(name)
    player.score = score
    return player
}

