package pt.isec.amov.mathit.model.data

import android.content.Context
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import pt.isec.amov.mathit.model.DataViewModel
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.levels.Levels
import pt.isec.amov.mathit.utils.MyCountDown
import kotlin.math.max

class CurrentGameData : java.io.Serializable {
    var tvsValues: MutableLiveData<ArrayList<String>>  = MutableLiveData(ArrayList())

    private lateinit var _timer : MutableLiveData<MyCountDown>

    var hasBeenInitiated : Boolean = false

    fun startCountDown(millisInFuture: Long, progressBar : ProgressBar, manager: ModelManager, context: Context){
        hasBeenInitiated = true
        progressBar.max = (millisInFuture / 1000).toInt()
        this._timer = MutableLiveData(MyCountDown(millisInFuture, progressBar, manager, context))
        _timer.value?.start()
    }

    fun addTime(time : Long){
        _timer.value?.addTime(time)
    }

    fun decreaseTime(time : Long){
        _timer.value?.decreaseTime(time)
    }

    fun getProgress() : Int? {
        return _timer.value?.getProgress()
    }

    fun assignRandomValues(level : Levels, tvs : ArrayList<TextView>){
        var cellCounter = 0
        val operations = level.operations
        val values : MutableLiveData<ArrayList<String>> by lazy {
            MutableLiveData<ArrayList<String>>().apply {
                value = ArrayList()
            }
        }

        for(view : TextView in tvs){
            //if the cell counter is between 5 and 7, it means its on the second row
            //if it is between 13 and 15, it means its on the fourth row
            //this rows only take operation signs
            if((cellCounter < 5 || cellCounter > 7) && (cellCounter < 13 || cellCounter > 15)){
                //one cell takes a number
                if(cellCounter % 2 == 0){
                    values.value?.add((1..level.maxNumb).shuffled().last().toString())
                    ++cellCounter
                    continue
                }

                //the other cell takes an operation sign
                values.value?.add(operations.shuffled().last().toString())
                ++cellCounter
                continue
            }

            values.value?.add(operations.shuffled().last().toString())
            ++cellCounter
        }

        Log.i("assignRandomValues", "" + values.value.toString())
        tvsValues = values
    }
}