package pt.isec.amov.mathit.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.fragments.GameBoardFragment
import pt.isec.amov.mathit.controllers.fragments.MultiplayerGameBoardFragment
import pt.isec.amov.mathit.databinding.ActivitySinglePlayerBinding
import pt.isec.amov.mathit.model.DataViewModel
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.CurrentGameData
import pt.isec.amov.mathit.model.data.levels.Levels
import java.util.ArrayList

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

    private val currentData : CurrentGameData = CurrentGameData()
    private val viewModel : DataViewModel by viewModels{
        DataViewModel.Factory(currentData)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            Log.i("SINGLE_PLAYER", "onCreate: ACTIVITY VAI LANÇAR FRAGMENT")
            intent.putExtra("data", manager)
            intent.putExtra("level", level)
            intent.putExtra("viewModel", viewModel)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<GameBoardFragment>(R.id.fragment_container_view)
            }
        }
    }
}

