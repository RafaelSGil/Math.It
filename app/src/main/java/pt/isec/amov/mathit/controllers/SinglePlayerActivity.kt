package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent.*
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import net.objecthunter.exp4j.ExpressionBuilder
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.fragments.GameBoardFragment
import pt.isec.amov.mathit.databinding.ActivitySinglePlayerBinding
import pt.isec.amov.mathit.model.ModelManager


class SinglePlayerActivity : AppCompatActivity(){
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, SinglePlayerActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var binding: ActivitySinglePlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

/*        val bundle = bundleOf("some_int" to 0)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<GameBoardFragment>(binding.fragmentContainerView, args = bundle)
        }*/

    }

}