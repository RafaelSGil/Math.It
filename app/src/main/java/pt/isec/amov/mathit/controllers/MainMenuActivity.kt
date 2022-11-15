package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.databinding.ActivityMainMenuBinding
import pt.isec.amov.mathit.model.ModelManager

class MainMenuActivity : AppCompatActivity() {
    companion object{
        private lateinit var manager : ModelManager

        fun getNewIntent(context : Context, manager : ModelManager) : Intent{
            val intent = Intent(context, MainMenuActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var binding : ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ButtonSinglePlayer.setOnClickListener{ _ ->
            manager.goSinglePlayerState(this, manager)
        }

        binding.ButtonMultiPlayer.setOnClickListener{view ->
            Snackbar.make(view,"Coming Soon", Snackbar.LENGTH_LONG).show()
        }
    }
}