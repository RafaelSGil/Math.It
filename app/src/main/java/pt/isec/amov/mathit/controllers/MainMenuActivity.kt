package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.databinding.ActivityMainBinding
import pt.isec.amov.mathit.model.ModelManager

class MainMenuActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, MainMenuActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var manager : ModelManager

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manager = ModelManager(getSharedPreferences("Math-It_Preferences",0))
        registerHandlers()

    }

    override fun onResume() {
        manager.goStartState(this, manager)
        super.onResume()
    }

    private fun registerHandlers() {
        binding.ButtonSinglePlayer.setOnClickListener {
            manager.goSinglePlayerState(this, manager)
        }

        binding.ButtonMultiPlayer.setOnClickListener {
            manager.goWaitMultiStartState(this, manager)
        }

        binding.btnSettings.setOnClickListener {
            manager.goProfileState(this, manager)
        }

        binding.BtnUsers.setOnClickListener{
            manager.goTop5State(this, manager)
        }
    }
}

