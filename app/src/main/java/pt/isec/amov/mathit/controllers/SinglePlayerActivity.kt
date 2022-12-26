package pt.isec.amov.mathit.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.fragments.GameBoardFragment
import pt.isec.amov.mathit.controllers.fragments.MultiplayerGameBoardFragment
import pt.isec.amov.mathit.databinding.ActivitySinglePlayerBinding
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.levels.Levels

class SinglePlayerActivity : AppCompatActivity(){
    companion object{
        private var manager : ModelManager? = null
        private lateinit var level : Levels

        fun getNewIntent(context : Context, manager : ModelManager, level : Levels) : Intent {
            val intent = Intent(context, SinglePlayerActivity::class.java)
            this.manager = manager
            this.level = level
            return intent
        }
    }

    private lateinit var binding: ActivitySinglePlayerBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.putExtra("data", manager)
        intent.putExtra("level", level)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<GameBoardFragment>(R.id.fragment_container_view)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("DEBUG-AMOV", "onPause: exiting singleplayer activity")
        finish()
    }
}

