package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.fragments.GameBoardFragment
import pt.isec.amov.mathit.databinding.ActivityMultiPlayerBinding
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.levels.Levels

class MultiPlayerActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null
        private lateinit var level : Levels

        fun getNewIntent(context : Context, manager : ModelManager, level : Levels) : Intent {
            val intent = Intent(context, MultiPlayerActivity::class.java)
            this.manager = manager
            this.level = level
            return intent
        }
    }
    private lateinit var binding: ActivityMultiPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.putExtra("data", manager)
        intent.putExtra("level", level)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<GameBoardFragment>(R.id.fragment_container_view)
        }
    }
}