package com.savestudents.features

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.savestudents.components.R.id
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.savestudents.components.snackbar.SnackBarCustomType
import com.savestudents.components.snackbar.SnackBarCustomView
import com.savestudents.core.utils.InitialScreenTypes
import com.savestudents.features.databinding.ActivityNavigationBinding

class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    private var controller: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navController) as NavHostFragment
        controller = navHostFragment.navController

        handleUserLogger()
        setContentView(binding.root)
    }

    private fun handleUserLogger() {
        val intent = intent.extras
        val initialScreen = intent?.getSerializable(INITIAL_SCREEN_TYPES) as InitialScreenTypes

        when (initialScreen) {
            InitialScreenTypes.HOME -> {
                controller?.run {
                    navigate(R.id.homeFragment)
                    backQueue.clear()
                }
            }

            InitialScreenTypes.LOGIN -> {
                controller?.navigate(R.id.loginFragment)
            }
        }
    }

    private fun handleMenuOptions() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                id.nav_schedule -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    controller?.navigate(R.id.curriculumFragment)
                    true
                }

                id.nav_notification -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    controller?.navigate(R.id.curriculumFragment)
                    true
                }

                id.nav_config -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    controller?.navigate(R.id.curriculumFragment)
                    true
                }

                id.notification -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    controller?.navigate(R.id.curriculumFragment)
                    true
                }
                else -> false
            }
        }
    }
    fun handleDrawerMenu() {
        supportActionBar?.dispatchMenuVisibilityChanged(true)
        visibleToolbar(true)
        val actionBarToggle = ActionBarDrawerToggle(this, binding.drawerLayout, 0, 0)
        binding.run {
            toolbar.withNotification = true
            toolbar.title = ""
            drawerLayout.addDrawerListener(actionBarToggle)
            actionBarToggle.syncState()

            toolbar.root.setNavigationOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
            handleMenuOptions()
        }
    }

    fun goBackPressed(action: () -> Unit) {
        binding.toolbar.root.setNavigationOnClickListener {
            action()
        }
    }

    fun handleTitleToolbar(title: String) {
        visibleToolbar(true)
        binding.toolbar.withNotification = false
        binding.toolbar.title = title
    }

    fun visibleToolbar(visible: Boolean) {
        binding.toolbar.isVisible = visible
    }

    fun showSnackBar(title: String, snackBarCustomType: SnackBarCustomType) {
        SnackBarCustomView.show(
            view = binding.navView,
            title = title,
            snackBarCustomType = snackBarCustomType
        )
    }

    companion object {
        private const val INITIAL_SCREEN_TYPES = "initial_screen_types"
        fun newInstance(context: Context, initialScreen: InitialScreenTypes): Intent {
            val intent = Intent(context, NavigationActivity::class.java)
            val bundle = Bundle().apply {
                this.putSerializable(INITIAL_SCREEN_TYPES, initialScreen)
            }
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }
}