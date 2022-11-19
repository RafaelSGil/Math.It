package pt.isec.amov.mathit.model.data

import pt.isec.amov.mathit.model.data.levels.Levels
import android.annotation.SuppressLint
import android.content.SharedPreferences

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

    init {
        playerName = sharedPreferences?.getString("username", "Player" + (1..99999).shuffled().last())
    }

    private fun savePlayerName(username: String) {
        editor?.putString("username", username)
        editor?.commit()
    }

    var level: Levels? = null

    fun getNextLevel(): Levels? {
        return level?.getNextLevel(level)
    }
}

