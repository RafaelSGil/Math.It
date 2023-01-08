package pt.isec.amov.mathit.controllers.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.telecom.ConnectionRequest
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
import pt.isec.amov.mathit.utils.MyCountDown
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.properties.Delegates

class ClientGameFragment : Fragment(R.layout.game_board), View.OnTouchListener {
    private lateinit var binding : GameBoardBinding
    private var tvs : ArrayList<TextView> = ArrayList()
    private var operationSigns : ArrayList<String> = ArrayList()
    private var bestCombination : ArrayList<TextView> = ArrayList()
    private var secondBestCombination : ArrayList<TextView> = ArrayList()
    private var idsSelected : ArrayList<TextView> = ArrayList()
    private var valuesSelected : ArrayList<String> = ArrayList()

    private val swipe = 700
    private val swipeVelocity = 100
    private lateinit var gestureDetector: GestureDetector

    private var NUM_OF_BOARDS_GENERATED = 50

    private lateinit var manager: ModelManager
    private lateinit var level : Levels

    private var goNextLevel : Boolean = false

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
            timer = MyCountDown(level.timeToComplete*1000, viewModel, manager, contextActivity)
            timer?.start()
            binding.pbTimer.progress = viewModel.timer.value!!
            viewModel.initiateViewModel()
        }

        viewModel.timer.observe(viewLifecycleOwner){
            binding.pbTimer.progress = viewModel.timer.value!!
        }

        viewModel.tvsValues.observe(viewLifecycleOwner){
            val values = viewModel.tvsValues.value!!

            for ((counter, v: TextView) in tvs.withIndex()) {
                v.text = values[counter]
            }
        }

        manager.addPropertyChangeListener(ConnectionManager.STARTING_MULTIPLAYER){
            viewModel.assignRandomValues(ConnectionManager.levelData?.board!!)
            level = when(ConnectionManager.levelData?.level!!){
                1 -> Levels.LEVEL1
                2 -> Levels.LEVEL2
                3 -> Levels.LEVEL3
                4 -> Levels.LEVEL4
                5 -> Levels.LEVEL5
                6 -> Levels.LEVEL6
                7 -> Levels.LEVEL7
                8 -> Levels.LEVEL8
                else -> Levels.LEVEL1
            }
            "${resources.getString(R.string.level)} $level".also { binding.tvLevel.text = it }
        }

        manager.addPropertyChangeListener(ConnectionManager.NEXT_BOARD){
            viewModel.assignRandomValues(ConnectionManager.nextBoard?.newBoard!!)
            viewModel.updateBoardIndex(ConnectionManager.nextBoard?.nextBoardIndex!!)
            manager.addPoints(ConnectionManager.nextBoard?.pointsEarned!!)
            "${resources.getString(R.string.points)} ${manager.getPoints()}".also { binding.tvPoints.text = it }
        }

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

            for(v: TextView in idsSelected){
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


            if (idsSelected.size >= 5 && valuesSelected.size >= 5){
                ConnectionManager.askForNextBoard(viewModel.boardIndex.value!!, valuesSelected,
                                                    manager.getLocalPlayerName()!!, manager.getPoints(),
                                                    level.toString().toInt())
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