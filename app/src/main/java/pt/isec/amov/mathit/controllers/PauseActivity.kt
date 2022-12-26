package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.databinding.ActivityNextLevelBinding
import pt.isec.amov.mathit.databinding.ActivityPauseBinding
import pt.isec.amov.mathit.model.ModelManager

class PauseActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null
        private var time : Int = 0

        fun getNewIntent(context : Context, manager : ModelManager, time : Int) : Intent {
            val intent = Intent(context, PauseActivity::class.java)
            this.manager = manager
            this.time = time
            return intent
        }
    }

    private lateinit var binding: ActivityPauseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPauseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnResume.apply {
            setOnClickListener {
                manager?.goNextLevelState(context, manager!!, time)
            }
        }

        binding.btnMainMenu.apply {
            setOnClickListener {
                manager?.goStartState(context, manager!!)
            }
        }
    }
}