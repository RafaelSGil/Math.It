package pt.isec.amov.mathit.model.data

import pt.isec.amov.mathit.model.data.levels.Levels
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.databinding.ObservableField
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

@SuppressLint("CommitPrefEdits")
class Data(sharedPreferences: SharedPreferences?) : java.io.Serializable{
    var editor = sharedPreferences?.edit()
    var playerName: String? = null
        set(value) {
            if (!value.isNullOrEmpty()) {
                field = value
                savePlayerName(value)
            }
        }
    companion object {
        private const val sharedPUsername = "username"
        private const val sharedPMultiplayerScore = "multiplayer_score"
        private const val sharedPSingleplayerScore = "singleplayer_score"
    }

    var multiplayerScore: Int = 0
        set(value){
            if(value == 0){
                field = 0
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
            field += value
        }

    init {
        playerName = sharedPreferences?.getString(sharedPUsername, "Player" + (1..99999).shuffled().last())
        if (sharedPreferences != null) {
            multiplayerScore = sharedPreferences.getInt(sharedPMultiplayerScore, 0)
        }
        if (sharedPreferences != null) {
            singleplayerScore = sharedPreferences.getInt(sharedPSingleplayerScore, 0)
        }
    }

    fun resetScoresLevels(){
        singleplayerScore = 0
        multiplayerScore = 0
        level = Levels.LEVEL1
    }

    private fun savePlayerName(username: String) {
        editor?.putString("username", username)
        editor?.commit()
    }

    fun setSinglePlayerScore(newScore: Int) {
        if(newScore > singleplayerScore!!) {
            singleplayerScore = newScore
            editor?.putInt(sharedPSingleplayerScore, singleplayerScore!!)
            updateDataInFirestore()
        }
    }
    fun setMultiPlayerScore(newScore: Int) {
        if(newScore > multiplayerScore!!) {
            multiplayerScore = newScore
            editor?.putInt(sharedPMultiplayerScore, multiplayerScore!!)
            updateDataInFirestore()
        }
    }

    private fun updateDataInFirestore() {
        val db = Firebase.firestore
        val vM = db.collection("Top5_Multiplayer").document(playerName!!)
        vM.get(Source.SERVER)
            .addOnSuccessListener {
                val exists = it.exists()
                if (!exists)
                    return@addOnSuccessListener
                vM.update("score",multiplayerScore!!)
            }
            .addOnFailureListener { _ ->
                addMultiplayerScoreDataToFirestore()
            }
        val vS = db.collection("Top5_Singleplayer").document(playerName!!)
        vS.get(Source.SERVER)
            .addOnSuccessListener {
                val exists = it.exists()
                if (!exists)
                    return@addOnSuccessListener
                vS.update("score",singleplayerScore!!)
            }
            .addOnFailureListener { _ ->
                addSingleplayerScoreDataToFirestore()
            }
    }

    private fun addMultiplayerScoreDataToFirestore() {
        val db = Firebase.firestore
        val values = hashMapOf(
            "score" to multiplayerScore,
        )
        db.collection("Top5_Multiplayer").document(playerName!!).set(values)
    }
    private fun addSingleplayerScoreDataToFirestore() {
        val db = Firebase.firestore
        val values = hashMapOf(
            "score" to singleplayerScore,
        )
        db.collection("Top5_Singleplayer").document(playerName!!).set(values)
    }

    private var level: Levels = Levels.LEVEL1

    fun getLevel(): Levels {
        if(singleplayerScore > level.maxNumb && level != Levels.LEVEL8){
            level = level.getNextLevel(level)
        }
        return level
    }
}

