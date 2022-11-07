package pt.isec.amov.mathit

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.isec.amov.mathit.databinding.ActivitySinglePlayerBinding

class SinglePlayerActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySinglePlayerBinding

    private var tvs : ArrayList<TextView> = ArrayList()
    private var operationSigns : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        //create an array with all
        operationSigns.addAll(arrayOf("+", "-", "*", "/"))

        assignRandomValues()
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
                    view.text = (0..100).shuffled().last().toString()
                    ++cellCounter
                    continue
                }

                //the other cell takes an operation sign
                view.text = operationSigns[(0..3).shuffled().last()]
                ++cellCounter
                continue
            }

            view.text = operationSigns[(0..3).shuffled().last()]
            ++cellCounter
        }
    }
}