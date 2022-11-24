package pt.isec.amov.mathit.controllers.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import net.objecthunter.exp4j.ExpressionBuilder
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.databinding.GameBoardBinding
import kotlin.math.abs


class GameBoardFragment : Fragment(R.layout.game_board){
    private lateinit var binding : GameBoardBinding

    private var tvs : ArrayList<TextView> = ArrayList()
    private var operationSigns : ArrayList<String> = ArrayList()
    private var idsSelected : ArrayList<TextView> = ArrayList()
    private var bestCombination : ArrayList<TextView> = ArrayList()
    private var secondBestCombination : ArrayList<TextView> = ArrayList()

    private lateinit var onSwipeTouchListener : OnSwipeTouchListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GameBoardBinding.inflate(layoutInflater)
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

        onSwipeTouchListener = OnSwipeTouchListener(context, tvs)

        operationSigns.addAll(arrayOf("+", "-", "*", "/"))

        assignRandomValues()

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
    }


    private fun assignRandomValues(){
        var cellCounter = 0
        for(view : TextView in tvs){
            //if the cell counter is between 5 and 7, it means its on the second row
            //if it is between 13 and 15, it means its on the fourth row
            //this rows only take operation signs
            if((cellCounter < 5 || cellCounter > 7) && (cellCounter < 13 || cellCounter > 15)){
                //one cell takes a number
                if(cellCounter % 2 == 0){
                    view.text = (0..10).shuffled().last().toString()
                    ++cellCounter
                    continue
                }

                //the other cell takes an operation sign
                view.text = operationSigns[0]
                ++cellCounter
                continue
            }

            view.text = operationSigns[0]
            ++cellCounter
        }

        calculateBestCombination()
    }

    class OnSwipeTouchListener(ctx: Context?, mainView: ArrayList<TextView>) : OnTouchListener {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
        private var gestureDetector: GestureDetector = GestureDetector(ctx, GestureListener())
        lateinit var context: Context

        init {
            gestureDetector = GestureDetector(ctx, GestureListener())
            //mainView.setOnTouchListener(this)

            for(view : View in mainView){
                view.setOnTouchListener(this)
            }

            if (ctx != null) {
                context = ctx
            }
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            return gestureDetector.onTouchEvent(event)
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
                        if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                            result = true
                        }
                    } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom()
                        } else {
                            onSwipeTop()
                        }
                        result = true
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                return result
            }
        }

        fun onSwipeRight() {
            Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show()
            //onSwipe!!.swipeRight()
        }

        fun onSwipeLeft() {
            Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show()
            //onSwipe!!.swipeLeft()
        }

        fun onSwipeTop() {
            Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show()
            //onSwipe!!.swipeTop()
        }

        fun onSwipeBottom() {
            Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show()
            //onSwipe!!.swipeBottom()
        }

        interface onSwipeListener {
            fun swipeRight()
            fun swipeTop()
            fun swipeBottom()
            fun swipeLeft()
        }

        var onSwipe: onSwipeListener? = null
    }
}