package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.amov.mathit.databinding.ActivityMultiPlayerWaitStartBinding
import pt.isec.amov.mathit.model.ModelManager

class MultiPlayerWaitStartActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, MultiPlayerWaitStartActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var binding: ActivityMultiPlayerWaitStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiPlayerWaitStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerHandlers()
    }

    private fun registerHandlers() {
        //todo register all missing handlers
    }
}