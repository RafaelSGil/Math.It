package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.fragments.GameBoardFragment
import pt.isec.amov.mathit.databinding.ActivitySinglePlayerBinding
import pt.isec.amov.mathit.model.ModelManager


class SinglePlayerActivity : AppCompatActivity(){
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, SinglePlayerActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var binding: ActivitySinglePlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.putExtra("data", manager)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<GameBoardFragment>(R.id.fragment_container_view)
        }

        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(100)
                        runOnUiThread {
                            binding.tvPoints.text = manager?.getPoints().toString()
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }

        thread.start()

    }

}

