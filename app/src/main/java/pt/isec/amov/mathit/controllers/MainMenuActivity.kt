package pt.isec.amov.mathit.controllers

import android.Manifest.permission.ACCESS_WIFI_STATE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.databinding.ActivityMainBinding
import pt.isec.amov.mathit.model.ModelManager
import pt.isec.amov.mathit.utils.NetUtils

class MainMenuActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, MainMenuActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var manager : ModelManager

    private lateinit var binding : ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manager = ModelManager(getSharedPreferences("Math-It_Preferences",0))
        registerHandlers()

    }

    override fun onResume() {
        manager.goStartState(this, manager)
        manager.reset()
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun registerHandlers() {
        binding.ButtonSinglePlayer.setOnClickListener {
            manager.goSinglePlayerState(this, manager)
        }

        binding.ButtonMultiPlayer.setOnClickListener {
            if (checkSelfPermission(ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                requestPermissionLauncher.launch(ACCESS_WIFI_STATE)
                //requestPermissions(arrayOf(ACCESS_WIFI_STATE), 0)
                Snackbar.make(it, "No permissions to access internet", 2000).show()
            }
            if (!NetUtils.verifyNetworkStateV3(this)) {
                Toast.makeText(this,"No network available",Toast.LENGTH_LONG).show()
                //finish()
                return@setOnClickListener
            }
            manager.goWaitMultiStartState(this, manager)
        }


        binding.btnSettings.setOnClickListener {
            manager.goProfileState(this, manager)
        }

        binding.BtnUsers.setOnClickListener{
            manager.goTop5State(this, manager)
        }
    }

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
}

