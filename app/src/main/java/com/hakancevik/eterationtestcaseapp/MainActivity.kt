package com.hakancevik.eterationtestcaseapp

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hakancevik.eterationtestcaseapp.databinding.ActivityMainBinding
import com.hakancevik.eterationtestcaseapp.extension.gone
import com.hakancevik.eterationtestcaseapp.extension.hide
import com.hakancevik.eterationtestcaseapp.extension.show
import com.hakancevik.eterationtestcaseapp.ui.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_favorite, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    fun updateToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun showBackIcon(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)

        if (show) {
            val backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
            backArrow?.let {
                DrawableCompat.setTint(it, ContextCompat.getColor(this, R.color.white))
                binding.toolbar.navigationIcon = it
            }
        }
    }


    fun updateBadgeCount(count: Int) {
        val cartBadge = binding.navView.getBadge(R.id.navigation_cart)
        if (count > 0) {
            if (cartBadge == null) {
                binding.navView.getOrCreateBadge(R.id.navigation_cart).apply {
                    number = count
                }
            } else {
                cartBadge.number = count
            }
        } else {
            binding.navView.removeBadge(R.id.navigation_cart)
        }
    }

    fun removeBadge(){
        binding.navView.removeBadge(R.id.navigation_cart)
    }


}