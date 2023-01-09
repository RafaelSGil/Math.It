package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import org.json.JSONObject
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.fragments.ClientGameFragment
import pt.isec.amov.mathit.controllers.fragments.HostGameFragment
import pt.isec.amov.mathit.databinding.ActivityMultiPlayerBinding
import pt.isec.amov.mathit.model.ConnectionManager
import pt.isec.amov.mathit.model.DataViewModel
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.model.data.CurrentGameData

class MultiPlayerActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null
        private lateinit var mode: String

        fun getNewIntent(context : Context, manager : ModelManager, mode: String) : Intent {
            val intent = Intent(context, MultiPlayerActivity::class.java)
            this.manager = manager
            this.mode = mode
//            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            return intent
        }
    }
    private lateinit var binding: ActivityMultiPlayerBinding

    private val currentData : CurrentGameData = CurrentGameData()
    private val viewModel : DataViewModel by viewModels{
        DataViewModel.Factory(currentData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("Multiplayer Mode", "" + mode)

        if(savedInstanceState == null){
            when(mode){
                "host" -> {
                    intent.putExtra("data", manager)
                    intent.putExtra("viewModel", viewModel)

                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add<HostGameFragment>(R.id.fragment_container_view)
                    }

                    ConnectionManager.sendDataToAllClients("start_game")
                }
                else -> {
                    intent.putExtra("data", manager)
                    intent.putExtra("viewModel", viewModel)

                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add<ClientGameFragment>(R.id.fragment_container_view)
                    }
                }
            }
        }
    }
}