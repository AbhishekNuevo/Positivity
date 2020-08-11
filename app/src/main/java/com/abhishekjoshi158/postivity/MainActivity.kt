package com.abhishekjoshi158.postivity

import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.abhishekjoshi158.postivity.viewmodels.HomeScreenViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private val TAG = MainActivity::class.java.simpleName
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val width = displayMetrics.widthPixels
   // val width = this.resources.configuration.screenWidthDp
    MainActivity.SCREEN_WIDTH = width

    val topLevelDestinations = setOf(
      R.id.positivityFragment,
      R.id.favouriteFragment
    )
    val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
    val navController = findNavController(R.id.nav_host_fragment)
    appBarConfiguration = AppBarConfiguration(topLevelDestinations)
    bottomNavigation.setupWithNavController(navController)
    setupActionBarWithNavController(navController, appBarConfiguration)
    val androidId: String = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
    val deviceID  = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8"))).toString()
    DEVICE_ID = deviceID
    Log.d(TAG,"device id $DEVICE_ID")
  }

  companion object {
    var SCREEN_WIDTH: Int = 400
    var DEVICE_ID = ""
  }

  override fun onSupportNavigateUp(): Boolean {

    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp(appBarConfiguration)
      || super.onSupportNavigateUp()
  }


}
