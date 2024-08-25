package ru.wip.testapp

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.wip.testapp.domain.navigation.NavigationCommand
import ru.wip.testapp.domain.navigation.Navigator
import ru.wip.testapp.domain.navigation.Screens
import ru.wip.testapp.feature.main.ui.MainFragment
import ru.wip.testapp.feature.points.ui.PointsFragment

class MainActivity : AppCompatActivity() {

  private val navigator by inject<Navigator>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupNavigation()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      navigator.back()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  private fun setupNavigation() {
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController

    navController.graph = navController.createGraph(startDestination = Screens.MAIN, builder = {
      fragment<MainFragment>(Screens.MAIN)
      fragment<PointsFragment>(Screens.POINTS)
    })

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.CREATED) {
        launch {
          navigator.observe.collectLatest { command ->
            dispatchNavigatorCommand(navController, command)
          }
        }

        launch {
          navHostFragment.childFragmentManager.addOnBackStackChangedListener {
            val backStackEntryCount = navHostFragment.childFragmentManager.backStackEntryCount
            supportActionBar?.setDisplayHomeAsUpEnabled(backStackEntryCount > 0)
          }
        }
      }
    }

    onBackPressedDispatcher.addCallback {
      navigator.back()
    }

    val backStackEntryCount = navHostFragment.childFragmentManager.backStackEntryCount
    supportActionBar?.setDisplayHomeAsUpEnabled(backStackEntryCount > 0)
  }

  private fun dispatchNavigatorCommand(navController: NavController, command: NavigationCommand) {
    when (command) {
      is NavigationCommand.NavigateTo -> {
        navController.navigate(command.screen)
      }

      is NavigationCommand.Back -> {
        if (!navController.popBackStack()) {
          finish()
        }
      }
    }
  }
}