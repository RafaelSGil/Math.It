package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.databinding.ActivityGameOverBinding
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.levels.Levels

class GameOverActivity : AppCompatActivity() {
    companion object{
        private lateinit var manager : ModelManager
        private var isSinglePlayer : Boolean = false

        fun getNewIntent(context : Context, manager : ModelManager, isSinglePlayer : Boolean) : Intent {
            val intent = Intent(context, GameOverActivity::class.java)
            this.manager = manager
            this.isSinglePlayer = isSinglePlayer
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            return intent
        }
    }

    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("GAME", "OVER")

        when(isSinglePlayer){
            true ->{"${resources.getString(R.string.points)} ${manager.getPointsSinglePlayer()}".also { binding.tvPoints.text = it }}

            false -> {"${resources.getString(R.string.level)} ${manager.getPointsMultiPlayer()}".also { binding.tvPoints.text = it }}
        }

        "Level: ${manager.getLevel().toString()}".also { binding.tvLevel.text = it }

        binding.btnMainMenu.setOnClickListener {
            Log.i("CLICK", "OLA")
            manager.goStartState(this, manager)
        }

        binding.btnSave.setOnClickListener {
            if(isSinglePlayer){

                manager.sendSinglePlayerScoreToFirebase()
                manager.goStartState(this, manager)
                return@setOnClickListener
            }

            manager.sendMultiPlayerScoreToFirebase()
            manager.goStartState(this, manager)
        }
    }
}