package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.databinding.ActivityTop5Binding
import pt.isec.amov.mathit.model.ModelManager

class Top5Activity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, Top5Activity::class.java)
            this.manager = manager
            return intent
        }
    }

    private var MODE = 0

    private lateinit var binding: ActivityTop5Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTop5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        registerHandlers()
        binding.top5Listview.emptyView

    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun updateList() {
        when(MODE) {
            0 -> listTop5Singleplayer()
            1 -> listTop5Multiplayer()
        }
    }

    private fun listTop5Singleplayer() {

    }

    private fun listTop5Multiplayer() {

    }

    private fun registerHandlers() {
        binding.btnToggleMode.setOnClickListener {
            MODE = when(binding.btnToggleMode.isChecked) {
                true -> { Snackbar.make(it, "Showing SinglePlayer Top5", 1000).show()
                    0
                }
                false -> { Snackbar.make(it, "Showing MultiPlayer Top5", 1000).show()
                    1
                }
            }
        }
    }
}