package com.abhishekjoshi158.postivity

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.abhishekjoshi158.postivity.fragments.FavouriteFragment
import com.abhishekjoshi158.postivity.fragments.PositivityFragment
import com.abhishekjoshi158.postivity.fragments.SideNavItem
import com.abhishekjoshi158.postivity.interfaces.FavouriteInterface
import com.abhishekjoshi158.postivity.notification.FCMServices
import com.abhishekjoshi158.postivity.utilities.STORAGE_REQUEST_CODE
import com.google.android.material.bottomnavigation.BottomNavigationView

import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavigation: BottomNavigationView
    private val TAG = MainActivity::class.java.simpleName
    private val _permissionGranted = MutableLiveData<Int>()
    val permissionGranted: LiveData<Int> = _permissionGranted
    private var drawerLayout: DrawerLayout? = null
    private var pRunnable: Runnable? = null
    private val positivityFragment = PositivityFragment()
    private val favouriteFragment = FavouriteFragment()
    private var activeBottomNavigationFragment: Fragment = positivityFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDrawer()

        deviceConfig()

        val topLevelDestinations = setOf(
            R.id.positivityFragment,
            R.id.favouriteFragment
        )

        bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations, drawerLayout)

        bottomNavigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationListener()

    }

    private fun setDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.openDrawer, R.string.closeDrawer)
        drawerLayout?.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                Log.d("Home Menu ", " closed ")
                if (pRunnable != null) {
                    Handler().post(pRunnable)
                    pRunnable = null
                }
            }

            override fun onDrawerOpened(drawerView: View) {

            }

        })

    }

    private fun deviceConfig() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        SCREEN_WIDTH = width
        val androidId: String =
            Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
        val deviceID = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8"))).toString()
        DEVICE_ID = deviceID
        Log.d(TAG, "device id $DEVICE_ID")
        FCMServices().getNewToken()
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

    fun closeDrawer(item: SideNavItem) {
        drawerLayout?.closeDrawer(GravityCompat.START)
        when (item.id) {
            1 -> {
                pRunnable = object : Runnable {
                    override fun run() {
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.nav_host_fragment, PositivityFragment())
//            transaction.commit()
                        Log.d(TAG, "drawer closed positivity click")
                        bottomNavigation.selectedItemId = R.id.positivityFragment
                    }

                }


            }
            2 -> {

                pRunnable = Runnable {
//          val transaction = supportFragmentManager.beginTransaction()
//          transaction.replace(R.id.nav_host_fragment,favouriteFragment)
//          transaction.commit()
                    Log.d(TAG, "drawer closed favourite click")
                    bottomNavigation.selectedItemId = R.id.favouriteFragment
                }

            }
            3 -> {

            }
            4 -> {

            }
            else -> {

            }
        }

    }


    fun setUpPermission() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.storage_permission)
                builder.setTitle(R.string.storage_permission_title)
                builder.setPositiveButton(R.string.ok) { dialog, id ->
                    makeRequest()
                }
                builder.setNegativeButton(R.string.cancel) { dialog, which ->

                }

            } else {
                makeRequest()
            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_REQUEST_CODE
        )
    }

    private fun bottomNavigationListener() {

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.positivityFragment -> {
                    supportFragmentManager.beginTransaction().hide(activeBottomNavigationFragment)
                        .show(positivityFragment).commit()
                    activeBottomNavigationFragment = positivityFragment
                    supportActionBar?.title = "Positivity"
                }

                R.id.favouriteFragment -> {
                    var alreadyPresent = false
                    supportFragmentManager.fragments.forEachIndexed { index, fragment ->
                        if (fragment is FavouriteFragment) {
                            alreadyPresent = true
                        }
                    }
                    if (alreadyPresent) {
                        supportFragmentManager.beginTransaction()
                            .hide(activeBottomNavigationFragment)
                            .show(favouriteFragment).commit()

                    } else {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.nav_host_fragment, favouriteFragment)
                            .hide(activeBottomNavigationFragment)
                            .show(favouriteFragment).commit()

                    }
                    supportActionBar?.title = "Favourite"
                    activeBottomNavigationFragment = favouriteFragment
                }

            }

            true
        }
        bottomNavigation.setOnNavigationItemReselectedListener {

        }
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
