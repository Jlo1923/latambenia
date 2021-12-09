package com.example.semillaviva

import android.annotation.SuppressLint
import android.content.*
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import com.example.semillaviva.data.preferences.UserPreferences
import com.example.semillaviva.databinding.ActivityMenuBinding
import com.example.semillaviva.ui.auth.AuthActivity
import com.example.semillaviva.ui.perfil.SlideshowFragment
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MenuActivity : AppCompatActivity(), SlideshowFragment.OnCallbackReceived{

    private lateinit var appBarConfiguration: AppBarConfiguration
    var drawerLayout: DrawerLayout? = null
    var navController: NavController? = null
    private lateinit var navView: NavigationView
    private lateinit var preferences :UserPreferences
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var idDriver: String = ""
    var urlPhoto: String = ""
    var idPosition: String = ""
    var idTravel: String = ""
    var phonePidotax: String = "3184562840"

    var statusDriver: String = ""
    var firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var btnInstagram: ImageView
    private lateinit var btnFacebook: ImageView
    private lateinit var btnYoutobe: ImageView


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMenuBinding = DataBindingUtil.setContentView(this, R.layout.activity_menu)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView  = findViewById(R.id.nav_view)
        navView.itemIconTintList = null
        navController = findNavController(R.id.nav_host_fragment)
        loadPreferences()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_gallery,
                R.id.nav_home,
                R.id.nav_slideshow,
                R.id.nav_sesion
            ), drawerLayout
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView.setupWithNavController(navController!!)
        navView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout!!.closeDrawers()
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.nav_gallery -> navController!!.navigate(R.id.nav_gallery)
                R.id.nav_home -> navController!!.navigate(R.id.nav_home)

                R.id.nav_profile -> navController!!.navigate(R.id.nav_slideshow)
                R.id.nav_sesion -> {
                    preferences.save(UserPreferences.IS_LOGGED, false)
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
        loadHeader()
    }

    private fun loadPreferences() {
        preferences = UserPreferences(this)
        firstName = preferences.getValueString(UserPreferences.USER_NOMBRE).toString()
        lastName = preferences.getValueString(UserPreferences.USER_PHONE).toString()
        urlPhoto = preferences.getValueString(UserPreferences.USER_IMAGEN).toString()

    }
    @SuppressLint("SetTextI18n")
    private fun loadHeader() {
        val txtName: TextView = navView.getHeaderView(0).findViewById(R.id.userName)
        val txtEmail: TextView = navView.getHeaderView(0).findViewById(R.id.userPhone)
        val imageView: CircleImageView = navView.getHeaderView(0).findViewById(R.id.imguser)

        Picasso.with(this).load(urlPhoto).into(imageView)
        txtName.text = firstName
        txtEmail.text = lastName
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            return super.onBackPressed()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun Update() {
        loadHeader()
    }

}