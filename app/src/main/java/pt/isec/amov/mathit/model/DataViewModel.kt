package pt.isec.amov.mathit.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.mathit.model.data.CurrentGameData

class DataViewModel(private val data : CurrentGameData) : ViewModel(), java.io.Serializable {
    val tvsValues : LiveData<ArrayList<String>>
        get() = data.tvsValues

    fun refreshValues(values: ArrayList<String>) = data.refreshValues(values)

    class Factory(private val data: CurrentGameData) : ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DataViewModel(data) as T
        }
    }
}