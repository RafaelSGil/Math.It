package pt.isec.amov.mathit.model.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.LinkedList

class CurrentGameData : java.io.Serializable {
    var tvsValues: MutableLiveData<ArrayList<String>>  = MutableLiveData(ArrayList())

    private var _currentLevelBoards : MutableLiveData<LinkedList<ArrayList<String>>> = MutableLiveData(LinkedList(ArrayList()))
    val currentLevelBoards : LiveData<LinkedList<ArrayList<String>>>
        get() = _currentLevelBoards

    private var _bestCombinations : MutableLiveData<LinkedList<ArrayList<String>>> = MutableLiveData(LinkedList(ArrayList()))
    val bestCombinations : LiveData<LinkedList<ArrayList<String>>>
        get() = _bestCombinations

    private var _secondBestCombinations : MutableLiveData<LinkedList<ArrayList<String>>> = MutableLiveData(LinkedList(ArrayList()))
    val secondBestCombinations : LiveData<LinkedList<ArrayList<String>>>
        get() = _secondBestCombinations

    private var _timer : MutableLiveData<Int> = MutableLiveData(0)
    val timer : LiveData<Int>
        get() = _timer

    var hasBeenInitiated : Boolean = false

    private var _inWhichBoardAmI : MutableLiveData<Int> = MutableLiveData(0)

    val inWhichBoardAmI : LiveData<Int>
        get() = _inWhichBoardAmI

    private var _boardIndex : MutableLiveData<Int> = MutableLiveData(0)

    val boardIndex : LiveData<Int>
        get() = _boardIndex

    private var _currentLevel : MutableLiveData<Int> = MutableLiveData(1)
    val currentLevel : LiveData<Int>
        get() = _currentLevel

    private var _multiplayerMode : MutableLiveData<String> = MutableLiveData("")
    val multiplayerMode : LiveData<String>
        get() = _multiplayerMode

    fun setMode(str : String){
        _multiplayerMode.value = str
    }

    fun updateCurrentLeve(level : Int){
        _currentLevel.value = level
    }

    fun updateBoardIndex(index : Int){
        _boardIndex.value = index
    }

    fun updateTimer(time : Int){
        _timer.value = time
    }

    fun assignRandomValues(tvs : ArrayList<String>){
        Log.i("ELE ETÀ A ENTRAR", "assignRandomValues: ")
        tvsValues.value?.clear()
        tvsValues.value = tvs
    }

    fun initiateViewModel(){
        hasBeenInitiated = true
    }

    fun newBoar(board : ArrayList<String>){
        _currentLevelBoards.value?.add(board)
    }

    fun newBestCombination(combination : ArrayList<String>){
        _bestCombinations.value?.add(combination)
    }

    fun newSecondBestCombination(combination : ArrayList<String>){
        _secondBestCombinations.value?.add(combination)
    }

    fun nextBoard(){
        _inWhichBoardAmI.value = _inWhichBoardAmI.value?.plus(1)
        Log.i("BEST", "")
        for(view : String in _bestCombinations.value?.get(_inWhichBoardAmI.value!!)!!){
            Log.i("", view)
        }
        Log.i("2º BEST", "")
        for(view : String in  _secondBestCombinations.value?.get(_inWhichBoardAmI.value!!)!!){
            Log.i("", view)
        }
    }
}