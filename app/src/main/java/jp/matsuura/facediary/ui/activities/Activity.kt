package jp.matsuura.facediary.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.ActivityBinding
import jp.matsuura.facediary.extenstions.showAlert

@AndroidEntryPoint
class Activity : AppCompatActivity() {

    private lateinit var binding: ActivityBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        initListener()
    }

    private fun initComponents() {
        // Note that the Toolbar defined in the layout has the id "my_toolbar"
        setSupportActionBar(binding.toolbar)

        binding.bottomBg.isVisible = false

        val navView: BottomNavigationView = binding.navBar
        navView.menu.getItem(1).isEnabled = false

        // refer to: https://issuetracker.google.com/issues/142847973
        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        val hasBottomViewIds = setOf(
            R.id.calendarFragment,
            R.id.timelineFragment,
        )

        val hasToolBarIds = setOf(
            R.id.calendarFragment,
            R.id.timelineFragment,
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (hasToolBarIds.contains(destination.id)) {
                supportActionBar?.show()
                binding.bottomBg.isVisible = true
            } else {
                supportActionBar?.hide()
                binding.bottomBg.isVisible = false
            }

            binding.navBar.isVisible = hasBottomViewIds.contains(destination.id)

        }
    }

    private fun initListener() {

        binding.fab.setOnClickListener {
            this.showAlert(
                titleRes = R.string.dummy,
                messageRes = R.string.dummy,
                positiveButtonRes = R.string.dummy,
                negativeButtonRes = R.string.dummy,
                onPositiveClick = { alertDialog ->
                    alertDialog.dismiss()
                },
                onNegativeClick = { alertDialog ->
                    alertDialog.dismiss()
                },
                isCancel = false
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}