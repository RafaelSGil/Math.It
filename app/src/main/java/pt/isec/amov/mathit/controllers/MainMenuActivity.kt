package pt.isec.amov.mathit.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.databinding.ActivityMainBinding
import pt.isec.amov.mathit.model.ModelManager

class MainMenuActivity : AppCompatActivity() {
    private lateinit var manager : ModelManager

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manager = ModelManager()
        registerHandlers()

    }

    private fun registerHandlers() {
        binding.ButtonSinglePlayer.setOnClickListener{
            manager.goSinglePlayerState(this, manager)
        }

        binding.ButtonMultiPlayer.setOnClickListener{
            Snackbar.make(it,"Coming Soon", Snackbar.LENGTH_LONG).show()
        }

        binding.ButtonProfile.setOnClickListener{
            manager.goProfileState(this, manager)
        }

        binding.btnSettings.setOnClickListener{
            Snackbar.make(it,"Coming Soon", Snackbar.LENGTH_LONG).show()
        }
    }
}