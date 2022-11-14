package pt.isec.amov.mathit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.amov.mathit.controllers.MainMenuActivity
import pt.isec.amov.mathit.databinding.ActivityMainBinding
import pt.isec.amov.mathit.model.ModelManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(MainMenuActivity.getNewIntent(this, ModelManager()))
    }
}