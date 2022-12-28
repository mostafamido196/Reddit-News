package com.alhamoly.redditnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.alhamoly.redditnews.R
import com.alhamoly.redditnews.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity()/*, NavController.OnDestinationChangedListener*/ {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
//    private val viewModel: BaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

    }

    /*override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        when (destination.id) {

            R.id.splashFragment,
            R.id.changeLogFragment,
            R.id.browserFragment,
            R.id.loginFragment,
            R.id.registerFragment,
            R.id.otpFragment -> visBNB(
                false
            )
            else -> visBNB(true)

        }

    }

    private fun visBNB(v: Boolean) {
        if (v) binding.bottomNavigation.visibility = View.VISIBLE
        else binding.bottomNavigation.visibility = View.GONE
    }*/

}