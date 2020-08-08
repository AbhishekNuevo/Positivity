package com.abhishekjoshi158.postivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

   private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      val width =  this.resources.configuration.screenWidthDp
      MainActivity.SCREEN_WIDTH = width

        val topLevelDestinations = setOf(R.id.positivityFragment,
        R.id.favouriteFragment)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController =  findNavController(R.id.nav_host_fragment)
      appBarConfiguration = AppBarConfiguration(topLevelDestinations)
      bottomNavigation.setupWithNavController(navController)
      setupActionBarWithNavController(navController,appBarConfiguration)

    }
  companion object {
    var SCREEN_WIDTH : Int = 0

  }

  override fun onSupportNavigateUp(): Boolean {

    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp(appBarConfiguration)
      || super.onSupportNavigateUp()
  }


}
