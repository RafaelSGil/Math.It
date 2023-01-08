package pt.isec.amov.mathit.model.data

import pt.isec.amov.mathit.model.data.levels.Levels
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@SuppressLint("CommitPrefEdits")
class Data(sharedPreferences: SharedPreferences?) : java.io.Serializable{
    var editor = sharedPreferences?.edit()
    var profilePicImagePath: String? = null
        set(value) {
            if(!value.isNullOrEmpty()) {
                field = value
                savePlayerProfilePic(value)
            }
        }

    var playerName: String? = null
        set(value) {
            if (!value.isNullOrEmpty() /* && value.length >= 5 */) {
                field = value
                savePlayerName(value)
            }
        }
    companion object {
        private const val sharedPUsername = "username"
        private const val sharedPProfilePic = "profile_pic"
        private const val sharedPMultiplayerScore = "multiplayer_score"
        private const val sharedPSingleplayerScore = "singleplayer_score"
    }

    var multiplayerScore: Int = 0
        set(value){
            Log.i("ADD", "POINTS: " + value)
            if(value == 0){
                field = 0
                return
            }
            if(value < 0){
                return
            }
            field += value
        }
    var singleplayerScore: Int = 0
        set(value){
            if(value == 0){
                field = 0
                return
            }
            if(value < 0){
                return
            }
            field += value
        }

    init {
        playerName = sharedPreferences?.getString(sharedPUsername, "Player" + (1..99999).shuffled().last())
        profilePicImagePath = sharedPreferences?.getString(sharedPProfilePic,"")
        multiplayerScore = sharedPreferences?.getLong(sharedPMultiplayerScore, 0)!!.toInt()
        singleplayerScore = sharedPreferences.getLong(sharedPSingleplayerScore, 0).toInt()
    }

    fun resetScoresLevels(){
        singleplayerScore = 0
        multiplayerScore = 0
        level = Levels.LEVEL1
    }

    private fun savePlayerName(username: String) {
        editor?.putString(sharedPUsername, username)
        editor?.commit()
    }

    private fun savePlayerProfilePic(filepath: String) {
        editor?.putString(sharedPProfilePic, filepath)
        editor?.commit()
    }

    fun setSinglePlayerScore() {
        addSingleplayerScoreDataToFirestore()
    }
    fun setMultiPlayerScore() {
        addMultiplayerScoreDataToFirestore()
    }

    private fun addMultiplayerScoreDataToFirestore() {
        val db = Firebase.firestore
        val values = hashMapOf(
            "player_name" to playerName,
            "score" to multiplayerScore,
        )
        db.collection("Top5_Multiplayer").add(values)
    }
    private fun addSingleplayerScoreDataToFirestore() {
        val db = Firebase.firestore
        val values = hashMapOf(
            "player_name" to playerName,
            "score" to singleplayerScore,
        )
        db.collection("Top5_Singleplayer").add(values)
    }

    private var level: Levels = Levels.LEVEL1

    fun getLevel(): Levels {
        if(singleplayerScore >= level.pointsToNextLevel && level != Levels.LEVEL8){
            level = level.getNextLevel(level)
        }
        return level
    }
}

