package pt.isec.amov.mathit.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.databinding.ActivityMultiplayerWaitForLobbyBinding
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
    }

    private fun registerHandlers() {
        binding.btnNextLevel.setOnClickListener{
            manager?.goMultiPlayerState(this, manager!!)
        }
    }
}