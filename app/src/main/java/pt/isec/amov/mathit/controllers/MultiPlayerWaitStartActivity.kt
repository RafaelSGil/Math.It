package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.amov.mathit.databinding.ActivityMultiplayerWaitStartBinding
import pt.isec.amov.mathit.model.ConnectionManager
import pt.isec.amov.mathit.model.ModelManager

class MultiPlayerWaitStartActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, MultiPlayerWaitStartActivity::class.java)
            this.manager = manager
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            return intent
        }
    }

    private lateinit var binding: ActivityMultiplayerWaitStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerWaitStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ConnectionManager.closeEverything()
        binding.availableGamesListView.emptyView = binding.emptyServersView
        registerHandlers()
    }

    override fun onResume() {
        super.onResume()
        manager?.goWaitMultiStartState(this, manager!!)
        manager?.startServerListener(binding.availableGamesListView)
    }

    private fun registerHandlers() {
        binding.btnCreateGame.setOnClickListener{
            createMultiplayerGame()
        }
        binding.availableGamesListView.setOnItemClickListener { _, _, position, _ ->
            manager?.goWaitForLobbyState(this, manager!!)
            manager?.startClient(position)
        }
    }

    private fun createMultiplayerGame() {
        manager?.startServer(applicationContext, manager?.getLevel()?.toString()!!.toInt())
        manager?.goWaitForLobbyState(this, manager!!)
    }

    override fun onPause() {
        super.onPause()
    }
}