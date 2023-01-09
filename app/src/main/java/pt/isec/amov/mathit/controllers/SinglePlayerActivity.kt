package pt.isec.amov.mathit.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.fragments.GameBoardFragment
import pt.isec.amov.mathit.databinding.ActivitySinglePlayerBinding
import pt.isec.amov.mathit.model.DataViewModel
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.CurrentGameData
import pt.isec.amov.mathit.model.data.levels.Levels

class SinglePlayerActivity : AppCompatActivity(){
    companion object{
        private var manager : ModelManager? = null
        private lateinit var level : Levels
        private var board: String? = null

        fun getNewIntent(context : Context, manager : ModelManager, level : Levels) : Intent {
            val intent = Intent(context, SinglePlayerActivity::class.java)
            this.manager = manager
            this.level = level
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            return intent
        }

        fun getNewIntent(context : Context, manager : ModelManager, board : String) : Intent {
            val intent = Intent(context, SinglePlayerActivity::class.java)
            this.manager = manager
            this.board = board
            this.level = manager.getLevel()
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
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
            if(board == null){
                intent.putExtra("data", manager)
                intent.putExtra("level", level)
                intent.putExtra("viewModel", viewModel)

                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<GameBoardFragment>(R.id.fragment_container_view)
                }
            }else{
                intent.putExtra("data", manager)
                intent.putExtra("board", board)
                intent.putExtra("level", level)
                intent.putExtra("viewModel", viewModel)

                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<GameBoardFragment>(R.id.fragment_container_view)
                }
            }
        }
    }
}

