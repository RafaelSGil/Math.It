package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.fragments.GameBoardFragment
import pt.isec.amov.mathit.controllers.fragments.MultiplayerGameBoardFragment
import pt.isec.amov.mathit.databinding.ActivityMultiPlayerBinding
import pt.isec.amov.mathit.model.ConnectionManager
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.levels.Levels

class MultiPlayerActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null
        private lateinit var mode: String

        fun getNewIntent(context : Context, manager : ModelManager, mode: String) : Intent {
            val intent = Intent(context, MultiPlayerActivity::class.java)
            this.manager = manager
            this.mode = mode
            return intent
        }
    }
    private lateinit var binding: ActivityMultiPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.putExtra("data", manager)
        intent.putExtra("mode", mode)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MultiplayerGameBoardFragment>(R.id.fragment_container_view)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}