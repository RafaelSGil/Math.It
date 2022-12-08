package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.amov.mathit.databinding.ActivityTop5Binding
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Player


class Top5Activity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, Top5Activity::class.java)
            this.manager = manager
            return intent
        }
    }
    private var top5MultiplayerList: ArrayList<Player>? = null
    private var top5SingleplayerList: ArrayList<Player>? = null

    enum class MODE {MULTIPLAYER, SINGLEPLAYER}

    private var mode = MODE.MULTIPLAYER

    private lateinit var binding: ActivityTop5Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTop5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        registerHandlers()
        binding.top5Listview.emptyView

        top5MultiplayerList = ArrayList()
        top5SingleplayerList = ArrayList()
    }

    override fun onResume() {
        super.onResume()
        getFirebaseData()
        updateList()
    }

    private fun getFirebaseData() {
        val db = Firebase.firestore
        db.collection("Top5_Multiplayer").orderBy("score", Query.Direction.DESCENDING).limit(5).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val name = document.id
                    val multiplayerScore = document.getLong("score")
                    val player = Player(name)
                    player.score = multiplayerScore!!
                    top5MultiplayerList?.add(player)
                }
            }
        db.collection("Top5_Singleplayer").orderBy("score", Query.Direction.DESCENDING).limit(5).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val name = document.id
                    val singleplayerScore = document.getLong("score")
                    val player = Player(name)
                    player.score = singleplayerScore!!
                    top5SingleplayerList?.add(player)
                }
            }
    }

    private fun updateList() {
        when(mode) {
            MODE.SINGLEPLAYER -> listTop5Singleplayer()
            MODE.MULTIPLAYER -> listTop5Multiplayer()
        }
    }

    private fun listTop5Singleplayer() {
        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, top5SingleplayerList!!.toArray())
        binding.top5Listview.adapter = arrayAdapter
    }

    private fun listTop5Multiplayer() {
        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, top5MultiplayerList!!.toArray())
        binding.top5Listview.adapter = arrayAdapter
    }

    private fun registerHandlers() {
        binding.btnToggleMode.setOnClickListener {
            mode = when(binding.btnToggleMode.isChecked) {
                true -> { Snackbar.make(it, "Showing Singleplayer Top5", 1000).show()
                    MODE.SINGLEPLAYER
                }
                false -> { Snackbar.make(it, "Showing Multiplayer Top5", 1000).show()
                    MODE.MULTIPLAYER
                }
            }
            updateList()
        }
    }

}