package pt.isec.amov.mathit.controllers

import android.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import pt.isec.amov.mathit.databinding.ActivityMultiplayerWaitForLobbyBinding
import pt.isec.amov.mathit.model.ConnectionManager
import pt.isec.amov.mathit.model.ModelManager

class MultiPlayerWaitForLobbyActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, MultiPlayerWaitForLobbyActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var binding: ActivityMultiplayerWaitForLobbyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerWaitForLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ConnectionManager.resetPlayers()
        registerHandlers()
    }

    override fun onResume() {
        super.onResume()
        binding.currentLevel.text = manager?.getLevel().toString()
        if(manager?.isHost() == true){
            binding.btnNextLevel.visibility = View.VISIBLE
        } else {
            binding.btnNextLevel.visibility = View.GONE
        }
        updatePlayersList()
        binding.lobbyPlayersListView.emptyView = binding.emptyPlayersView
    }

    private fun registerHandlers() {
        manager?.addPropertyChangeListener(ConnectionManager.PLAYERS_PROP) {
            updatePlayersList()
        }
        manager?.addPropertyChangeListener(ConnectionManager.STARTING_MULTIPLAYER) {
            startMultiplayer()
        }
        binding.btnNextLevel.setOnClickListener{
            manager?.goMultiPlayerState(this, manager!!, "host")
        }
    }

    private fun startMultiplayer() {
        manager?.goMultiPlayerState(this, manager!!, "client")
    }

    private fun updatePlayersList() {
        Log.i("DEBUG-AMOV", "updatePlayersList: updating list of players in lobby")
        val players = manager?.getConnectedPlayers() ?: return
        Log.i("Players", "" + players)
        players.sortedBy { player -> player.score }
        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this,
            R.layout.simple_list_item_1, players)
        binding.lobbyPlayersListView.adapter = arrayAdapter
    }
}