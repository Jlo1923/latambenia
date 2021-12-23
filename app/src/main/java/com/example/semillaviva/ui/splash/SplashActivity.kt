package com.example.semillaviva.ui.splash
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.example.semillaviva.MainActivity
import com.example.semillaviva.MenuActivity
import com.example.semillaviva.R
import dagger.hilt.android.AndroidEntryPoint
import com.example.semillaviva.data.preferences.UserPreferences
import com.example.semillaviva.databinding.ActivitySplashBinding
import com.example.semillaviva.ui.auth.AuthActivity

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val vm: SplashViewModel by viewModels()
    private var isLogged: Boolean = false

    private lateinit var preferences : UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_splash)

        loadPreferences()
     //   binding.viewModel = vm
      //  vm.timer.observe(this){

           exitSplashScreen()
        //}
    }

    private fun exitSplashScreen() {
        val handler = Handler()
        handler.postDelayed({
            if (isLogged){
                goToMapsActivity()
            }else{
                goToMainActivity()
            }
            this.finish()
        }, 3000)
    }
    private fun goToMapsActivity() {
        finish()
        startActivity(Intent(this, MenuActivity::class.java))
    }

    private fun loadPreferences() {
        preferences = UserPreferences(this)
        isLogged = preferences.getValueBoolean(UserPreferences.IS_LOGGED)
    }

    private fun goToMainActivity() {
        finish()
        startActivity(Intent(this, AuthActivity::class.java))
    }
}