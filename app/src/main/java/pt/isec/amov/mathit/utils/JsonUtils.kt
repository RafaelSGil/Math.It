package pt.isec.amov.mathit.utils

import org.json.JSONArray
import org.json.JSONObject
import pt.isec.amov.mathit.model.data.LevelData
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

fun jsonObjectToServerData(jsonObject: JSONObject): ServerData? {
    try {
        val name = jsonObject.getString("name")
        val host = jsonObject.getString("host")
        val port = jsonObject.getInt("port")
        return ServerData(host, port, name)
    } catch (_:java.lang.Exception) {
        return null
    }
}

fun playerToJson(player: Player) : JSONObject {
    val jsonObject = JSONObject().also {
        it.put("name", player.name)
        it.put("score", player.score)
    }
    return jsonObject
}

fun jsonObjectToPlayer(jsonObject: JSONObject) : Player? {
    return try {
        val name = jsonObject.getString("name")
        val score = jsonObject.getLong("score")
        val player = Player(name)
        player.score = score
        player
    } catch (_:java.lang.Exception) {
        null
    }
}

fun playerListToJsonObject(players: ArrayList<Player>) : JSONObject {
    val jsonArray = JSONArray()
    for(player in players) {
        jsonArray.put(playerToJson(player))
    }
    return JSONObject().also{it.put("players", jsonArray) }
}

fun playerJsonObjectToPlayerList(jsonObject: JSONObject) : ArrayList<Player> {
    return try {
        val players = ArrayList<Player>()
        val jsonArray = jsonObject.getJSONArray("players")
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            val score = jsonObject.getLong("score")
            players.add(Player(name).also { it.score = score })
        }
        players
    } catch (_:java.lang.Exception) {
        ArrayList()
    }
}

fun levelDataToJsonObject(levelData: LevelData) :JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("next_level", levelData.level)
    val jsonArray = JSONArray()
    for(value in levelData.values) {
        jsonArray.put(value)
    }
    jsonObject.put("values", jsonArray)
    return jsonObject
}

fun levelDatJsonObjectToLevelData(jsonObject: JSONObject): LevelData? {
    return try {
        val level = jsonObject.getInt("next_level")
        val values = jsonObject.getJSONArray("values")
        val valuesList =  ArrayList<String>()
        for (i in 0 until values.length()) {
            valuesList.add(values.get(i).toString())
        }
        LevelData(level, valuesList)
    } catch (_:java.lang.Exception) {
        null
    }
}

