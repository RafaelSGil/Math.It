package pt.isec.amov.mathit.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import pt.isec.amov.mathit.model.data.multiplayer.*

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
        it.put("player_name", player.name)
        it.put("score", player.score)
        it.put("level", player.level)
    }
    return jsonObject
}

fun jsonObjectToPlayer(jsonObject: JSONObject) : Player? {
    return try {
        val name = jsonObject.getString("player_name")
        val score = jsonObject.getLong("score")
        val level = jsonObject.getInt("level")
        val player = Player(name)
        player.score = score
        player.level = level
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
            val level = jsonObject.getInt("level")
            players.add(Player(name).also { it.score = score; it.level= level })
        }
        players
    } catch (_:java.lang.Exception) {
        ArrayList()
    }
}


fun levelDataToJsonObject(levelData: NextLevelData) :JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("next_level", levelData.level)
    val jsonArrayBoard = JSONArray()
    for(i in levelData.board){
        jsonArrayBoard.put(i)
    }
    jsonObject.put("tables", jsonArrayBoard)
    Log.i("DEBUG-AMOV", "levelDataToJsonObject: $jsonObject")
    return jsonObject
}

fun levelDataJsonObjectToLevelData(jsonObject: JSONObject): NextLevelData? {
    return try {
        val level = jsonObject.getInt("next_level")
        val jsonArrayBoard = jsonObject.getJSONArray("board")

        val board = ArrayList<String>()
        for(i in 0 until jsonArrayBoard.length()){
            board.add(jsonArrayBoard.getString(i))
        }
        return NextLevelData(level, board)
    } catch (_:java.lang.Exception) {
        null
    }
}

fun newMoveToJsonObject(move : NewMove) : JSONObject{
    val jsonObject = JSONObject().also {
        it.put("current_board", move.currentBoard)
        it.put("username", move.username)
        it.put("points", move.points)
        it.put("level", move.level)
    }

    val jsonArrayTiles = JSONArray()
    for(tile in move.tilesSelected){
        jsonArrayTiles.put(tile)
    }

    jsonObject.put("tiles_selected", jsonArrayTiles)

    return jsonObject
}

fun jsonObjectToNewMove(jsonObject: JSONObject) : NewMove?{
    return try {
        val currentBoard = jsonObject.getInt("current_board")
        val username = jsonObject.getString("username")
        val points = jsonObject.getInt("points")
        val level = jsonObject.getInt("level")
        val jsonArrayTiles = jsonObject.getJSONArray("tiles_selected")

        val tilesSelected = ArrayList<String>()
        for(i in 0 until jsonArrayTiles.length()){
            tilesSelected.add(jsonArrayTiles.getString(i))
        }

        return NewMove(currentBoard, tilesSelected, username, points, level)
    } catch (_:java.lang.Exception) {
        null
    }
}

fun jsonObjectToNextBoardData(jsonObject: JSONObject) : NextBoardData?{
    return try {
        val pointsEarned = jsonObject.getInt("points_earned")
        val boardIndex = jsonObject.getInt("next_board_index")

        val jsonArray = jsonObject.getJSONArray("next_board")
        val board = ArrayList<String>()
        for(i in 0 until jsonArray.length()){
            board.add(jsonArray.getString(i))
        }
        return NextBoardData(board, pointsEarned, boardIndex)
    } catch (_:java.lang.Exception){
        null
    }
}

fun nextBoardDataToJsonObject(nextBoardData: NextBoardData) : JSONObject{
    val jsonObject = JSONObject().also{
        it.put("points_earned", nextBoardData.pointsEarned)
        it.put("next_board_index", nextBoardData.nextBoardIndex)
    }

    val jsonArray = JSONArray()
    for(tiles in nextBoardData.newBoard){
        jsonArray.put(tiles)
    }
    jsonObject.put("next_board", jsonArray)

    return jsonObject
}
