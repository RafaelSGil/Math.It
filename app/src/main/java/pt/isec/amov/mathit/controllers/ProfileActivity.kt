package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.databinding.ActivityEditProfileBinding
import pt.isec.amov.mathit.model.ModelManager

class ProfileActivity : AppCompatActivity() {
    companion object{
        private var manager : ModelManager? = null

        fun getNewIntent(context : Context, manager : ModelManager) : Intent {
            val intent = Intent(context, ProfileActivity::class.java)
            this.manager = manager
            return intent
        }
    }

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerHandlers()

    }

    override fun onResume() {
        super.onResume()
        binding.editProfileNameTextInput.hint = manager?.getLocalPlayerName()
    }

    private fun registerHandlers() {
        binding.playerProfilePic.setOnClickListener{
            Snackbar.make(it, "Updating profile pic is coming soon", 1000).show()
        }

        binding.buttonSaveProfile.setOnClickListener{
            Snackbar.make(it, "Profile saved", 1000).show()
            manager?.changeLocalPlayerName(binding.editProfileNameTextInput.text.toString())
        }
    }
}