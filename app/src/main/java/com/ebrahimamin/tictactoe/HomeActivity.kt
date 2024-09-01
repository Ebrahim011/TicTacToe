package com.ebrahimamin.tictactoe

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import com.ebrahimamin.tictactoe.RoomFolder.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var toggleDarkMode: ImageButton
    private lateinit var logoutBtn: ImageButton
    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("UserTheme", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar: Toolbar = findViewById(R.id.appBar)
        setSupportActionBar(toolbar)

        logoutBtn = findViewById(R.id.SignOut)
        logoutBtn.setOnClickListener {
            val userSharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val editor = userSharedPreferences.edit()
            editor.clear()
            editor.apply()
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        /*        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.catogriesFragment2, R.id.favoriteFragment, R.id.searchFragment, R.id.profileFragment)
        )

        // Setup the Toolbar with NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
*/

        toggleDarkMode = findViewById(R.id.toggleDarkMode)
        updateToggleIcon()

        toggleDarkMode.setOnClickListener {
            val editor = sharedPreferences.edit()
            if (isDarkModeOn()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkMode", false)
                Log.i("Recipe", "Dark mode is off")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkMode", true)
                Log.i("Recipe", "Dark mode is on")
            }
            editor.apply()
            updateToggleIcon()
        }

    }
    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun updateToggleIcon() {
        if (isDarkModeOn()) {
            toggleDarkMode.setImageResource(R.drawable.ic_moon)
        } else {
            toggleDarkMode.setImageResource(R.drawable.ic_sun)
        }
    }

}