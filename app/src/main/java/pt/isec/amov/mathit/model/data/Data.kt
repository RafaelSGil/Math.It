package pt.isec.amov.mathit.model.data

import android.annotation.SuppressLint
import android.content.SharedPreferences

@SuppressLint("CommitPrefEdits")
class Data(sharedPreferences: SharedPreferences?) {
    var playerName: String? = null
        set(value) {
            if(!value.isNullOrEmpty()) {
                field = value
                savePlayerName(value)
            }
        }
    var editor = sharedPreferences?.edit()

    init {
        playerName = sharedPreferences?.getString("username", "Player" + (1..99999).shuffled().last())
    }

    private fun savePlayerName(username: String) {
        editor?.putString("username", username)
        editor?.commit()
    }
}