package com.abhishekjoshi158.postivity

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.abhishekjoshi158.postivity.interfaces.FavouriteInterface
import com.abhishekjoshi158.postivity.utilities.STORAGE_REQUEST_CODE
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity()  {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private val TAG = MainActivity::class.java.simpleName
  private val _permissionGranted = MutableLiveData<Int>()
  val permissionGranted : LiveData<Int> = _permissionGranted
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



  fun setUpPermission(){
      val permission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

       if(permission != PackageManager.PERMISSION_GRANTED){
         if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
           val builder = AlertDialog.Builder(this)
           builder.setMessage(R.string.storage_permission)
           builder.setTitle(R.string.storage_permission_title)
           builder.setPositiveButton(R.string.ok){ dialog, id ->
             makeRequest()
           }
           builder.setNegativeButton(R.string.cancel){ dialog, which ->

           }

         }else{
           makeRequest()
         }
       }
    }

  private fun makeRequest() {
    ActivityCompat.requestPermissions(this,
      arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
      STORAGE_REQUEST_CODE)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
      STORAGE_REQUEST_CODE -> {

        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
          _permissionGranted.value = STORAGE_REQUEST_CODE
        } else {
        }
      }
    }

  }

}
