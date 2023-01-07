package pt.isec.amov.mathit.model.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CurrentGameData : java.io.Serializable {
    var tvsValues: MutableLiveData<ArrayList<String>>  = MutableLiveData(ArrayList())

    private var _timer : MutableLiveData<Int> = MutableLiveData(0)
    val timer : LiveData<Int>
        get() = _timer

    var hasBeenInitiated : Boolean = false

    fun updateTimer(time : Int){
        _timer.value = time
    }

    fun assignRandomValues(tvs : ArrayList<String>){
        tvsValues.value?.clear()
        tvsValues.value = tvs
        Log.i("assignRandomValues", "" + tvsValues.value.toString())
    }

    fun initiateViewModel(){
        hasBeenInitiated = true
    }
}