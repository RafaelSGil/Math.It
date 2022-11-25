package pt.isec.amov.mathit.model.data

import pt.isec.amov.mathit.model.data.levels.Levels
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.ListView
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@SuppressLint("CommitPrefEdits")
class Data(sharedPreferences: SharedPreferences?) {
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
    private var multiplayerScore: Long? = null
    private var singleplayerScore: Long? = null

    init {
        playerName = sharedPreferences?.getString(sharedPUsername, "Player" + (1..99999).shuffled().last())
        multiplayerScore = sharedPreferences?.getLong(sharedPMultiplayerScore, 0)
        singleplayerScore = sharedPreferences?.getLong(sharedPSingleplayerScore, 0)
    }

    private fun savePlayerName(username: String) {
        editor?.putString("username", username)
        editor?.commit()
    }

    fun setSinglePlayerScore(newScore: Long) {
        if(newScore > singleplayerScore!!) {
            singleplayerScore = newScore
            editor?.putLong(sharedPSingleplayerScore, singleplayerScore!!)
            updateDataInFirestore()
        }
    }
    fun setMultiPlayerScore(newScore: Long) {
        if(newScore > multiplayerScore!!) {
            multiplayerScore = newScore
            editor?.putLong(sharedPMultiplayerScore, multiplayerScore!!)
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

    var level: Levels? = null

    fun getNextLevel(): Levels? {
        return level?.getNextLevel(level)
    }
}

