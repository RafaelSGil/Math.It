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

    val timer : LiveData<Int>
        get() = data.timer

    val hasBeenInitiated : Boolean
        get() = data.hasBeenInitiated

    fun assignRandomValues(tvs : ArrayList<String>) = data.assignRandomValues(tvs)

    fun updateTimer(time : Int) = data.updateTimer(time)

    fun initiateViewModel() = data.initiateViewModel()

    class Factory(private val data: CurrentGameData) : ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DataViewModel(data) as T
        }
    }
}