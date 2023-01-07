package pt.isec.amov.mathit.model

import android.content.Context
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.mathit.model.data.CurrentGameData
import pt.isec.amov.mathit.model.data.levels.Levels
import pt.isec.amov.mathit.utils.MyCountDown

class DataViewModel(private val data : CurrentGameData) : ViewModel(), java.io.Serializable {
    val tvsValues : LiveData<ArrayList<String>>
        get() = data.tvsValues


    val hasBeenInitiated : Boolean
        get() = data.hasBeenInitiated

    fun assignRandomValues(level : Levels, tvs : ArrayList<TextView>) = data.assignRandomValues(level, tvs)

    fun startCountDown(millisInFuture: Long, progressBar : ProgressBar, manager: ModelManager,
                       context: Context)
    = data.startCountDown(millisInFuture, progressBar, manager, context)

    fun addTime(time : Long) = data.addTime(time)
    fun decreaseTime(time : Long) = data.decreaseTime(time)
    fun getProgress() : Int? = data.getProgress()

    class Factory(private val data: CurrentGameData) : ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DataViewModel(data) as T
        }
    }
}