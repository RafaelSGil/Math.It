package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.databinding.ActivityMultiplayerWaitStartBinding
import pt.isec.amov.mathit.model.ModelManager

class MultiPlayerWaitStartActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, MultiPlayerWaitStartActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var binding: ActivityMultiplayerWaitStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerWaitStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            createMultiplayerGame();
        }
        binding.availableGamesListView.setOnItemClickListener { parent, view, position, id ->
            Log.i("DEBUG-AMOV", "registerHandlers: ${position}")
        }

    }

    private fun createMultiplayerGame() {
        manager?.startServer(applicationContext)
        manager?.goWaitForLobbyState(this, manager!!)
    }

    override fun onPause() {
        super.onPause()
        manager?.closeServerListener()
    }
}