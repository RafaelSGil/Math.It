package pt.isec.amov.mathit.controllers.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import net.objecthunter.exp4j.ExpressionBuilder
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.databinding.GameBoardBinding
import pt.isec.amov.mathit.model.ConnectionManager
import pt.isec.amov.mathit.model.DataViewModel
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.levels.Levels
import pt.isec.amov.mathit.model.data.multiplayer.NextLevelData
import pt.isec.amov.mathit.model.data.multiplayer.Player
import pt.isec.amov.mathit.model.data.multiplayer.PlayersData
import pt.isec.amov.mathit.utils.MyCountDown
import pt.isec.amov.mathit.utils.playerListToJsonObject
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.properties.Delegates

class HostGameFragment : Fragment(R.layout.game_board), View.OnTouchListener{
    private lateinit var binding : GameBoardBinding
    private var tvs : ArrayList<TextView> = ArrayList()
    private var operationSigns : ArrayList<String> = ArrayList()
    private var idsSelected : ArrayList<TextView> = ArrayList()
    private var valuesSelected : ArrayList<String> = ArrayList()

    private val swipe = 700
    private val swipeVelocity = 100
    private lateinit var gestureDetector: GestureDetector

    private var NUM_OF_BOARDS_GENERATED = 50

    private lateinit var manager: ModelManager
    private lateinit var level : Levels

    private var goNextLevel : Boolean = false

    private var points : Int by Delegates.observable(0){
            _, _, _ ->
        manager.addPoints(points)
        "${resources.getString(R.string.points)} ${manager.getPoints()}".also { binding.tvPoints.text = it }

        if(manager.getLevel() != level){
            goNextLevel = true
        }
        "${resources.getString(R.string.level)} ${manager.getLevel().toString()}".also { binding.tvLevel.text = it }
    }

    private lateinit var contextActivity: Context
    private var timer : MyCountDown? = null

    private lateinit var viewModel : DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameBoardBinding.inflate(layoutInflater)

        var i: Intent? = activity?.intent

        if (i != null) {
            manager = i.getSerializableExtra("data") as ModelManager
            viewModel = i.getSerializableExtra("viewModel") as DataViewModel
        }

        level = manager.getLevel()!!

        //add every text view to an array, to make it easier to iterate through each one
        tvs.add(binding.r1tv1)
        tvs.add(binding.r1tv2)
        tvs.add(binding.r1tv3)
        tvs.add(binding.r1tv4)
        tvs.add(binding.r1tv5)

        tvs.add(binding.r2tv1)
        tvs.add(binding.r2tv3)
        tvs.add(binding.r2tv5)

        tvs.add(binding.r3tv1)
        tvs.add(binding.r3tv2)
        tvs.add(binding.r3tv3)
        tvs.add(binding.r3tv4)
        tvs.add(binding.r3tv5)


        tvs.add(binding.r4tv1)
        tvs.add(binding.r4tv3)
        tvs.add(binding.r4tv5)

        tvs.add(binding.r5tv1)
        tvs.add(binding.r5tv2)
        tvs.add(binding.r5tv3)
        tvs.add(binding.r5tv4)
        tvs.add(binding.r5tv5)

        gestureDetector = GestureDetector(context, GestureListener())

        for (v : TextView in tvs){
            v.setOnTouchListener(this)
        }

        operationSigns.addAll(arrayOf("+", "-", "*", "/"))

        if (container != null) {
            contextActivity = container.context
        }

        if(viewModel.hasBeenInitiated){
            binding.pbTimer.max = (level.timeToComplete).toInt()
            timer = MyCountDown((viewModel.timer.value!! * 1000).toLong(), viewModel, manager, contextActivity)
            timer?.start()
            binding.pbTimer.progress = viewModel.timer.value!!
        }
        if(!viewModel.hasBeenInitiated){
            binding.pbTimer.max = (level.timeToComplete).toInt()
            generateBoards()
            ConnectionManager.setAllBoards(viewModel.currentLevelBoards.value!!)
            ConnectionManager.setBestCombinations(viewModel.bestCombinations.value!!)
            ConnectionManager.setSecondBestCombinations(viewModel.secondBestCombinations.value!!)
            timer = MyCountDown(level.timeToComplete*1000, viewModel, manager, contextActivity)
            timer?.start()
            binding.pbTimer.progress = viewModel.timer.value!!
            viewModel.initiateViewModel()

            val levelData = NextLevelData(level.toString().toInt(), viewModel.currentLevelBoards.value!![0])
            ConnectionManager.sendLevelDataToPlayers(levelData)
        }

        points = -1

        viewModel.timer.observe(viewLifecycleOwner){
            binding.pbTimer.progress = viewModel.timer.value!!
        }

        viewModel.inWhichBoardAmI.observe(viewLifecycleOwner){
            val values = viewModel.currentLevelBoards.value?.get(viewModel.inWhichBoardAmI.value!!)

            for ((counter, v: TextView) in tvs.withIndex()) {
                v.text = values?.get(counter).toString()
            }
        }

        ConnectionManager.updateLevel(level)

        return binding.root
    }

    override fun onDestroyView() {
        timer?.cancel()
        super.onDestroyView()
    }

    override fun onPause() {
        timer?.cancel()
        super.onPause()
    }

    private fun generateBoards(){
        for (i in 0..NUM_OF_BOARDS_GENERATED){
            assignRandomValues()
        }
    }

    private fun assignRandomValues(){
        var cellCounter = 0
        level = manager.getLevel()!!
        val operations = level.operations
        val values : ArrayList<String> = ArrayList()

        for(view : TextView in tvs){
            //if the cell counter is between 5 and 7, it means its on the second row
            //if it is between 13 and 15, it means its on the fourth row
            //this rows only take operation signs
            if((cellCounter < 5 || cellCounter > 7) && (cellCounter < 13 || cellCounter > 15)){
                //one cell takes a number
                if(cellCounter % 2 == 0){
                    values.add((1..manager.getLevel()?.maxNumb!!).shuffled().last().toString())
                    ++cellCounter
                    continue
                }

                //the other cell takes an operation sign
                values.add(operations.shuffled().last().toString())
                ++cellCounter
                continue
            }

            values.add(operations.shuffled().last().toString())
            ++cellCounter
        }

        viewModel.newBoar(values)
        calculateBestCombination(values)
    }

    private fun calculateBestCombination(values : ArrayList<String>){
        //ESTA FUNCAO DA ME VONTADE DE VOMITAR
        var first = Int.MIN_VALUE
        var second = Int.MIN_VALUE

        val secondBestCombination = ArrayList<String>()
        val bestCombination = ArrayList<String>()

        //1st row
        if (ExpressionBuilder(values[0] + values[1] + values[2] + values[3] + values[4]).build().evaluate() > first){
            first = ExpressionBuilder(values[0] + values[1] + values[2] + values[3] + values[4]).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(values[0] + values[1] + values[2] + values[3] + values[4]))
        }else if (ExpressionBuilder(values[0] + values[1] + values[2] + values[3] + values[4]).build().evaluate() > second
            && ExpressionBuilder(values[0] + values[1] + values[2] + values[3] + values[4]).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(values[0] + values[1] + values[2] + values[3] + values[4]))
        }

        //2nd row
        if (ExpressionBuilder(values[8] + values[9] + values[10] + values[11] + values[12]).build().evaluate() > first){
            first = ExpressionBuilder(values[8] + values[9] + values[10] + values[11] + values[12]).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(values[8] + values[9] + values[10] + values[11] + values[12]))
        }else if(ExpressionBuilder(values[8] + values[9] + values[10] + values[11] + values[12]).build().evaluate() > second
            && ExpressionBuilder(values[8] + values[9] + values[10] + values[11] + values[12]).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(values[8] + values[9] + values[10] + values[11] + values[12]))
        }

        //3rd row
        if (ExpressionBuilder(values[16] + values[17] + values[18] + values[19] + values[20]).build().evaluate() > first){
            first = ExpressionBuilder(values[16] + values[17] + values[18] + values[19] + values[20]).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(values[16] + values[17] + values[18] + values[19] + values[20]))
        }else if(ExpressionBuilder(values[16] + values[17] + values[18] + values[19] + values[20]).build().evaluate() > second
            && ExpressionBuilder(values[16] + values[17] + values[18] + values[19] + values[20]).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(values[16] + values[17] + values[18] + values[19] + values[20]))
        }

        //1st column
        if (ExpressionBuilder(values[0] + values[5] + values[8] + values[13] + values[16]).build().evaluate() > first){
            first = ExpressionBuilder(values[0] + values[5] + values[8] + values[13] + values[16]).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(values[0] + values[5] + values[8] + values[13] + values[16]))
        }else if(ExpressionBuilder(values[0] + values[5] + values[8] + values[13] + values[16]).build().evaluate() > second
            && ExpressionBuilder(values[0] + values[5] + values[8] + values[13] + values[16]).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(values[0] + values[5] + values[8] + values[13] + values[16]))
        }

        //2nd column
        if (ExpressionBuilder(values[2] + values[6] + values[10] + values[14] + values[18]).build().evaluate() > first){
            first = ExpressionBuilder(values[2] + values[6] + values[10] + values[14] + values[18]).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(values[2] + values[6] + values[10] + values[14] + values[18]))
        }else if(ExpressionBuilder(values[2] + values[6] + values[10] + values[14] + values[18]).build().evaluate() > second
            && ExpressionBuilder(values[2] + values[6] + values[10] + values[14] + values[18]).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(values[2] + values[6] + values[10] + values[14] + values[18]))
        }

        //3rd column
        if (ExpressionBuilder(values[4] + values[7] + values[12] + values[15] + values[20]).build().evaluate() > first){
            first = ExpressionBuilder(values[4] + values[7] + values[12] + values[15] + values[20]).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(values[4] + values[7] + values[12] + values[15] + values[20]))
        }else if(ExpressionBuilder(values[4] + values[7] + values[12] + values[15] + values[20]).build().evaluate() > second
            && ExpressionBuilder(values[4] + values[7] + values[12] + values[15] + values[20]).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(values[4] + values[7] + values[12] + values[15] + values[20]))
        }

        Log.i("calculateBest", "" + bestCombination)
        viewModel.newBestCombination(bestCombination)
        viewModel.newSecondBestCombination(secondBestCombination)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x

                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > swipe && abs(velocityX) > swipeVelocity) {
                        for(view : TextView in tvs){
                            val location = IntArray(2)
                            view.getLocationInWindow(location)
                            if(e1.rawY < location[1] + view.height && e1.rawY > location[1]){
                                idsSelected.add(view)
                                valuesSelected.add(view.text.toString())
                            }
                        }
                        result = true
                    }
                } else if (abs(diffY) > swipe && abs(velocityY) > swipeVelocity) {
                    for(view : TextView in tvs){
                        val location = IntArray(2)
                        view.getLocationInWindow(location)
                        if(e1.rawX < location[0] + view.width && e1.rawX > location[0]){
                            idsSelected.add(view)
                            valuesSelected.add(view.text.toString())
                        }
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            for(v:TextView in idsSelected){
                val previousDrawable = v.background
                v.setBackgroundResource(R.drawable.background_animation)
                val ani = v.background as AnimationDrawable
                v.setTextColor(Color.GRAY)
                ani.setExitFadeDuration(400)
                ani.setEnterFadeDuration(100)
                ani.start()
                thread {

                    Thread.sleep(250)

                    ani.stop()
                    v.background = previousDrawable
                    v.setTextColor(Color.BLACK)
                }
            }

            viewModel.inWhichBoardAmI.value!!

            if (idsSelected.size >= 5 && valuesSelected.size >= 5){
                var tiles : String = ""
                for (i in valuesSelected){
                    tiles += i
                }
                if (viewModel.bestCombinations.value?.get(viewModel.inWhichBoardAmI.value!!)?.containsAll(
                        listOf(tiles)
                    )!!){
                    Log.i("VALUES", "onFling: $valuesSelected")

                    idsSelected.clear()
                    valuesSelected.clear()

                    manager.addPoints(2)
                    "${resources.getString(R.string.points)} ${manager.getPoints()}".also { binding.tvPoints.text = it }

                    timer?.addTime(level.timeToIncrement.toLong())

                    PlayersData.updatePlayer(Player(manager.getLocalPlayerName()!!).also {
                        it.isWaiting = false
                        it.score = manager.getPoints().toLong()
                        it.level = manager.getLevel().toString().toInt()
                    })
                    ConnectionManager.sendDataToAllClients(playerListToJsonObject(PlayersData.getPlayers()).toString())

                    if(manager.getLevel() != level){
                        timer?.cancel()
                        manager.goWaitForLobbyState(contextActivity, manager)
                        return false
                    }
                    viewModel.nextBoard()
                    return result
                }
                if(viewModel.secondBestCombinations.value?.get(viewModel.inWhichBoardAmI.value!!)?.containsAll(
                        listOf(tiles)
                    )!!){
                    Log.i("VALUES", "onFling: $valuesSelected")

                    idsSelected.clear()
                    valuesSelected.clear()

                    manager.addPoints(1)
                    "${resources.getString(R.string.points)} ${manager.getPoints()}".also { binding.tvPoints.text = it }

                    timer?.addTime(level.timeToIncrement.toLong())

                    PlayersData.updatePlayer(Player(manager.getLocalPlayerName()!!).also {
                        it.isWaiting = false
                        it.score = manager.getPoints().toLong()
                        it.level = manager.getLevel().toString().toInt()
                    })

                    ConnectionManager.sendDataToAllClients(playerListToJsonObject(PlayersData.getPlayers()).toString())

                    if(manager.getLevel() != level){
                        timer?.cancel()
                        manager.goWaitForLobbyState(contextActivity, manager)
                        return false
                    }
                    viewModel.nextBoard()
                    return result
                }

                Log.i("VALUES", "onFling: $valuesSelected")

                timer?.decreaseTime(level.timeToDecrement.toLong())
                viewModel.nextBoard()
            }


            idsSelected.clear()
            valuesSelected.clear()

            Thread.sleep(250)
            return result
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(p1!!)
    }
}