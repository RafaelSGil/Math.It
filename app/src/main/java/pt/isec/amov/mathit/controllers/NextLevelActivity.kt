package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import pt.isec.amov.mathit.databinding.ActivityNextLevelBinding
import pt.isec.amov.mathit.model.ModelManager

class NextLevelActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, NextLevelActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var binding: ActivityNextLevelBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNextLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                when(binding.tvTime.text){
                    "5" -> binding.tvTime.text = "4"
                    "4" -> binding.tvTime.text = "3"
                    "3" -> binding.tvTime.text = "2"
                    "2" -> binding.tvTime.text = "1"
                    "1" -> binding.tvTime.text = "0"
                }
            }

            override fun onFinish() {
                goNext()
            }
        }.start()

        timer.cancel()
    }

    fun goNext(){
        manager?.redirectNextLevel(this, manager!!)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}