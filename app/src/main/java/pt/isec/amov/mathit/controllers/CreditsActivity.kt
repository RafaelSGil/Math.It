package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.amov.mathit.databinding.ActivityCreditsBinding
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.Player

class CreditsActivity : AppCompatActivity() {

    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, CreditsActivity::class.java)
            this.manager = manager
            return intent
        }

    }
    private lateinit var binding: ActivityCreditsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}