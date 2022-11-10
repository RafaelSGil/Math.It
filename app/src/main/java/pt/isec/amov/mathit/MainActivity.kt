package pt.isec.amov.mathit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ButtonSinglePlayer.setOnClickListener{_ ->
            startActivity(Intent(this, SinglePlayerActivity::class.java))
        }
        binding.ButtonMultiPlayer.setOnClickListener{view ->
            Snackbar.make(view,"Coming Soon", Snackbar.LENGTH_LONG).show()
        }
    }
}