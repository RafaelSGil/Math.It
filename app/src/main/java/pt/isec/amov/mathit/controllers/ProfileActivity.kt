package pt.isec.amov.mathit.controllers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import pt.isec.amov.mathit.R
import pt.isec.amov.mathit.controllers.fragments.createFileFromUri
import pt.isec.amov.mathit.controllers.fragments.setPic
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

    private var newImagePath : String? = null
    private var permissionsGranted = false
        set(value) {
            field = value
            binding.playerProfilePic.isEnabled = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerHandlers()
        verifyPermissions()
        updateProfilePicView(manager?.getLocalPlayerProfilePic())
    }

    override fun onResume() {
        super.onResume()
        binding.editProfileNameTextInput.hint = manager?.getLocalPlayerName()
    }

    private fun registerHandlers() {
        binding.playerProfilePic.apply {
            setOnClickListener {chooseImage()}
        }

        binding.buttonSaveProfile.setOnClickListener{
            Snackbar.make(it, "Profile saved", 1000).show()
            manager?.changeLocalPlayerName(binding.editProfileNameTextInput.text.toString())
            manager?.changeLocalPlayerProfilePic(newImagePath)
        }
    }


    private var startActivityForContentResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        newImagePath = uri?.let { createFileFromUri(this, it) }
        if(newImagePath != null)
            updateProfilePicView(newImagePath!!)
    }

    private fun updateProfilePicView(imagePath: String?) {
        Log.i("DEBUG-AMOV", "updateProfilePicView: $imagePath")
        if(imagePath == null)
            binding.playerProfilePic.background = ContextCompat.getDrawable(this,R.drawable.default_profile_image)

        binding.playerProfilePic.background = BitmapDrawable.createFromPath(imagePath)
    }

    private fun chooseImage() {
        startActivityForContentResult.launch("image/*")
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionsGranted = isGranted
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
        permissionsGranted = grantResults.values.any { it }
    }


    private fun verifyPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsGranted = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED

            if (!permissionsGranted)
                requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            return
        }
        // GALLERY, versões < API33
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsGranted = false
            requestPermissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        } else
            permissionsGranted = true
    }
}