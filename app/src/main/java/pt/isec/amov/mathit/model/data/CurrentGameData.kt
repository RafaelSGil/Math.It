package pt.isec.amov.mathit.model.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CurrentGameData : java.io.Serializable {
    private val _tvsValues : MutableLiveData<ArrayList<String>>  = MutableLiveData(ArrayList())
    val tvsValues : LiveData<ArrayList<String>>
        get() {
            return _tvsValues
        }


    fun refreshValues(values : ArrayList<String>){
        _tvsValues.value?.clear()
        _tvsValues.value?.addAll(values)
        Log.i("refreshValues", "" + _tvsValues.value.toString())
    }
}