package pt.isec.amov.mathit.controllers

import android.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import pt.isec.amov.mathit.databinding.ActivityMultiplayerWaitForLobbyBinding
import pt.isec.amov.mathit.model.ConnectionManager
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.multiplayer.PlayersData

class MultiPlayerWaitForLobbyActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, MultiPlayerWaitForLobbyActivity::class.java)
            this.manager = manager
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            return intent
        }
    }

    private lateinit var binding: ActivityMultiplayerWaitForLobbyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerWaitForLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        manager?.addPropertyChangeListener(ConnectionManager.INITIATE_FRAGMENT) {
            manager?.goMultiPlayerState(this, manager!!, "client")
        }
        binding.btnNextLevel.setOnClickListener{
            if(PlayersData.getPlayers().size < 2){
                return@setOnClickListener
            }
            manager?.goMultiPlayerState(this, manager!!, "host")
        }
    }

    private fun updatePlayersList() {
        val players = PlayersData.getPlayers()
        Log.i("Update Players", "" + players)
        players.sortedBy { player -> player.score }
        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this,
            R.layout.simple_list_item_1, players)
        binding.lobbyPlayersListView.adapter = arrayAdapter
    }
}