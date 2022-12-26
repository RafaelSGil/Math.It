package pt.isec.amov.mathit.utils

import android.content.Context
import android.os.CountDownTimer
import android.widget.ProgressBar
import pt.isec.amov.mathit.model.ModelManager

class MyCountDown(millisInFuture: Long, var progressBar : ProgressBar, var manager: ModelManager, var context: Context) {
    private var currentTimer = millisInFuture
    private var timeLimit = millisInFuture
    private var timer : CountDownTimer? = null

    private fun resetTime(){
        timer = object : CountDownTimer(currentTimer, 1000){
            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = (millisUntilFinished/1000).toInt()
                currentTimer -= 1
            }

            override fun onFinish() {
                manager.goGameOverState(context, manager)
            }

        }.start()
    }

    fun start(){
        resetTime()
    }

    fun addTime(timeToAdd : Long){
        if(currentTimer + (timeToAdd*1000) > timeLimit){
            currentTimer = timeLimit
            timer?.cancel()
            start()
            return
        }
        currentTimer += (timeToAdd*1000)
        timer?.cancel()
        start()
    }

    fun decreaseTime(timeToDecrease : Long){
        currentTimer -= (timeToDecrease*1000)
        timer?.cancel()
        start()
    }

    fun cancel(){
        timer?.cancel()
    }
}