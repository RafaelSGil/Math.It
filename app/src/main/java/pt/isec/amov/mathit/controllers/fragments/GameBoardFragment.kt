package pt.isec.amov.mathit.controllers.fragments

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnClickListener
import android.view.View.OnTouchListener
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionScene.Transition
import androidx.fragment.app.Fragment
import com.google.protobuf.Value
import net.objecthunter.exp4j.ExpressionBuilder
import org.w3c.dom.Text
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.SinglePlayerActivity
import pt.isec.amov.mathit.databinding.GameBoardBinding
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.levels.Levels
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.properties.Delegates


class GameBoardFragment : Fragment(R.layout.game_board), View.OnTouchListener {
    private lateinit var binding : GameBoardBinding

    private var tvs : ArrayList<TextView> = ArrayList()
    private var operationSigns : ArrayList<String> = ArrayList()
    private var bestCombination : ArrayList<TextView> = ArrayList()
    private var secondBestCombination : ArrayList<TextView> = ArrayList()
    private var idsSelected : ArrayList<TextView> = ArrayList();

    private val swipe = 700
    private val swipeVelocity = 100
    private lateinit var gestureDetector: GestureDetector

    private lateinit var manager: ModelManager
    private lateinit var level : Levels

    private var goNextLevel : Boolean = false

    private var points : Int by Delegates.observable(0){
        _, _, _ ->
        manager.addPoints(points)
        "Points: ${manager.getPoints()}".also { binding.tvPoints.text = it }

        if(manager.getLevel() != level){
            //manager.goNextLevelState(contextActivity, manager)
            goNextLevel = true
        }
        "Level: ${manager.getLevel().toString()}".also { binding.tvLevel.text = it }

//        assignRandomValues()
    }

    private lateinit var contextActivity: Context

    private lateinit var timer : CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (container != null) {
            contextActivity = container.context
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GameBoardBinding.inflate(layoutInflater)

        var i : Intent? = activity?.intent

        if (i != null) {
            manager = i.getSerializableExtra("data") as ModelManager
            level = i.getSerializableExtra("level") as Levels
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

        for(v : TextView in tvs){
            v.setOnTouchListener(this)
        }

        operationSigns.addAll(arrayOf("+", "-", "*", "/"))

        assignRandomValues()

        points = -1

        binding.pbTimer.max = level.timeToComplete.toInt()

        timer = object : CountDownTimer(level.timeToComplete*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.pbTimer.progress = (millisUntilFinished/1000).toInt()
            }

            override fun onFinish() {
                manager.goStartState(contextActivity, manager)
            }
        }.start()
    }

    private fun calculateBestCombination(){
        //ESTA FUNCAO DA ME VONTADE DE VOMITAR
        var first = Int.MIN_VALUE
        var second = Int.MIN_VALUE

        //1st row
        if (ExpressionBuilder(binding.r1tv1.text.toString() + binding.r1tv2.text.toString() + binding.r1tv3.text.toString() + binding.r1tv4.text.toString() + binding.r1tv5.text.toString()).build().evaluate() > first){
            first = ExpressionBuilder(binding.r1tv1.text.toString() + binding.r1tv2.text.toString() + binding.r1tv3.text.toString() + binding.r1tv4.text.toString() + binding.r1tv5.text.toString()).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(binding.r1tv1, binding.r1tv2, binding.r1tv3, binding.r1tv4, binding.r1tv5))
        }else if (ExpressionBuilder(binding.r1tv1.text.toString() + binding.r1tv2.text.toString() + binding.r1tv3.text.toString() + binding.r1tv4.text.toString() + binding.r1tv5.text.toString()).build().evaluate() > second
            && ExpressionBuilder(binding.r1tv1.text.toString() + binding.r1tv2.text.toString() + binding.r1tv3.text.toString() + binding.r1tv4.text.toString() + binding.r1tv5.text.toString()).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(binding.r1tv1, binding.r1tv2, binding.r1tv3, binding.r1tv4, binding.r1tv5))
        }

        //2nd row
        if (ExpressionBuilder(binding.r3tv1.text.toString() + binding.r3tv2.text.toString() + binding.r3tv3.text.toString() + binding.r3tv4.text.toString() + binding.r3tv5.text.toString()).build().evaluate() > first){
            first = ExpressionBuilder(binding.r3tv1.text.toString() + binding.r3tv2.text.toString() + binding.r3tv3.text.toString() + binding.r3tv4.text.toString() + binding.r3tv5.text.toString()).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(binding.r3tv1, binding.r3tv2, binding.r3tv3, binding.r3tv4, binding.r3tv5))
        }else if(ExpressionBuilder(binding.r3tv1.text.toString() + binding.r3tv2.text.toString() + binding.r3tv3.text.toString() + binding.r3tv4.text.toString() + binding.r3tv5.text.toString()).build().evaluate() > second
            && ExpressionBuilder(binding.r3tv1.text.toString() + binding.r3tv2.text.toString() + binding.r3tv3.text.toString() + binding.r3tv4.text.toString() + binding.r3tv5.text.toString()).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(binding.r3tv1, binding.r3tv2, binding.r3tv3, binding.r3tv4, binding.r3tv5))
        }

        //3rd row
        if (ExpressionBuilder(binding.r5tv1.text.toString() + binding.r5tv2.text.toString() + binding.r5tv3.text.toString() + binding.r5tv4.text.toString() + binding.r5tv5.text.toString()).build().evaluate() > first){
            first = ExpressionBuilder(binding.r5tv1.text.toString() + binding.r5tv2.text.toString() + binding.r5tv3.text.toString() + binding.r5tv4.text.toString() + binding.r5tv5.text.toString()).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(binding.r5tv1, binding.r5tv2, binding.r5tv3, binding.r5tv4, binding.r5tv5))
        }else if(ExpressionBuilder(binding.r5tv1.text.toString() + binding.r5tv2.text.toString() + binding.r5tv3.text.toString() + binding.r5tv4.text.toString() + binding.r5tv5.text.toString()).build().evaluate() > second
            && ExpressionBuilder(binding.r5tv1.text.toString() + binding.r5tv2.text.toString() + binding.r5tv3.text.toString() + binding.r5tv4.text.toString() + binding.r5tv5.text.toString()).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(binding.r5tv1, binding.r5tv2, binding.r5tv3, binding.r5tv4, binding.r5tv5))
        }

        //1st column
        if (ExpressionBuilder(binding.r1tv1.text.toString() + binding.r2tv1.text.toString() + binding.r3tv1.text.toString() + binding.r4tv1.text.toString() + binding.r5tv1.text.toString()).build().evaluate() > first){
            first = ExpressionBuilder(binding.r1tv1.text.toString() + binding.r2tv1.text.toString() + binding.r3tv1.text.toString() + binding.r4tv1.text.toString() + binding.r5tv1.text.toString()).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(binding.r1tv1, binding.r2tv1, binding.r3tv1, binding.r4tv1, binding.r5tv1))
        }else if(ExpressionBuilder(binding.r1tv1.text.toString() + binding.r2tv1.text.toString() + binding.r3tv1.text.toString() + binding.r4tv1.text.toString() + binding.r5tv1.text.toString()).build().evaluate() > second
            && ExpressionBuilder(binding.r1tv1.text.toString() + binding.r2tv1.text.toString() + binding.r3tv1.text.toString() + binding.r4tv1.text.toString() + binding.r5tv1.text.toString()).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(binding.r1tv1, binding.r2tv1, binding.r3tv1, binding.r4tv1, binding.r5tv1))
        }

        //2nd column
        if (ExpressionBuilder(binding.r1tv3.text.toString() + binding.r2tv3.text.toString() + binding.r3tv3.text.toString() + binding.r4tv3.text.toString() + binding.r5tv3.text.toString()).build().evaluate() > first){
            first = ExpressionBuilder(binding.r1tv3.text.toString() + binding.r2tv3.text.toString() + binding.r3tv3.text.toString() + binding.r4tv3.text.toString() + binding.r5tv3.text.toString()).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(binding.r1tv3, binding.r2tv3, binding.r3tv3, binding.r4tv3, binding.r5tv3))
        }else if(ExpressionBuilder(binding.r1tv3.text.toString() + binding.r2tv3.text.toString() + binding.r3tv3.text.toString() + binding.r4tv3.text.toString() + binding.r5tv3.text.toString()).build().evaluate() > second
            && ExpressionBuilder(binding.r1tv3.text.toString() + binding.r2tv3.text.toString() + binding.r3tv3.text.toString() + binding.r4tv3.text.toString() + binding.r5tv3.text.toString()).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(binding.r1tv3, binding.r2tv3, binding.r3tv3, binding.r4tv3, binding.r5tv3))
        }

        //3rd column
        if (ExpressionBuilder(binding.r1tv5.text.toString() + binding.r2tv5.text.toString() + binding.r3tv5.text.toString() + binding.r4tv5.text.toString() + binding.r5tv5.text.toString()).build().evaluate() > first){
            first = ExpressionBuilder(binding.r1tv5.text.toString() + binding.r2tv5.text.toString() + binding.r3tv5.text.toString() + binding.r4tv5.text.toString() + binding.r5tv5.text.toString()).build().evaluate().toInt()
            secondBestCombination.clear()
            secondBestCombination.addAll(bestCombination)
            bestCombination.clear()
            bestCombination.addAll(listOf(binding.r1tv5, binding.r2tv5, binding.r3tv5, binding.r4tv5, binding.r5tv5))
        }else if(ExpressionBuilder(binding.r1tv5.text.toString() + binding.r2tv5.text.toString() + binding.r3tv5.text.toString() + binding.r4tv5.text.toString() + binding.r5tv5.text.toString()).build().evaluate() > second
            && ExpressionBuilder(binding.r1tv5.text.toString() + binding.r2tv5.text.toString() + binding.r3tv5.text.toString() + binding.r4tv5.text.toString() + binding.r5tv5.text.toString()).build().evaluate().toInt() != first){
            second = first
            secondBestCombination.clear()
            secondBestCombination.addAll(listOf(binding.r1tv5, binding.r2tv5, binding.r3tv5, binding.r4tv5, binding.r5tv5))
        }

        Log.i("BEST", "")
        for(view : TextView in bestCombination){
            Log.i("", view.text.toString())
        }
        Log.i("2º BEST", "")
        for(view : TextView in secondBestCombination){
            Log.i("", view.text.toString())
        }
    }


    private fun assignRandomValues(){
        var cellCounter = 0
        level = manager.getLevel()!!
        val operations = level.operations

        for(view : TextView in tvs){
            //if the cell counter is between 5 and 7, it means its on the second row
            //if it is between 13 and 15, it means its on the fourth row
            //this rows only take operation signs
            if((cellCounter < 5 || cellCounter > 7) && (cellCounter < 13 || cellCounter > 15)){
                //one cell takes a number
                if(cellCounter % 2 == 0){
                    view.text = (1..manager.getLevel()?.maxNumb!!).shuffled().last().toString()
                    ++cellCounter
                    continue
                }

                //the other cell takes an operation sign
                view.text = operations.shuffled().last().toString()
                ++cellCounter
                continue
            }

            view.text = operations.shuffled().last().toString()
            ++cellCounter
        }

        calculateBestCombination()
    }

    inner class GestureListener : SimpleOnGestureListener() {


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

                        }
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            var startColor = Color.YELLOW
            var endColor = Color.WHITE

            /*val mColors = arrayOf(ColorDrawable(Color.GRAY), ColorDrawable(Color.GREEN) , ColorDrawable(Color.GRAY))
            val mTransition = TransitionDrawable(mColors)
            idsSelected.get(0).background = mTransition
            mTransition.startTransition(200)*/


            for(v:TextView in idsSelected){
                v.setBackgroundResource(R.drawable.background_animation)
                val ani = v.background as AnimationDrawable
                v.setTextColor(Color.GRAY)
                ani.setEnterFadeDuration(100)
                ani.setExitFadeDuration(400)
                ani.start()

                thread {
                    Thread.sleep(500)

                    ani.stop()
                }
            }

            if (idsSelected.size >= 5){
                if (idsSelected.containsAll(bestCombination)){
                    idsSelected.clear()

                    points = 2

                    if(goNextLevel){
                        manager.goNextLevelState(contextActivity, manager)
                        timer.cancel()
                        return false
                    }
                    assignRandomValues()
                    return result
                }
                if(idsSelected.containsAll(secondBestCombination)){
                    Log.i("RESULT: ", "SECOND BEST")

                    idsSelected.clear()

                    points = 1

                    if(goNextLevel){
                        manager.goNextLevelState(contextActivity, manager)
                        timer.cancel()
                        return false
                    }
                    assignRandomValues()
                    return result
                }

                assignRandomValues()

            }


            idsSelected.clear()
            return result
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(p1!!)
    }
}