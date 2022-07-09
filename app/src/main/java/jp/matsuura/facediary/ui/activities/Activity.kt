package jp.matsuura.facediary.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.ActivityBinding

@AndroidEntryPoint
class Activity : AppCompatActivity() {

    private lateinit var binding: ActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)
        binding = ActivityBinding.inflate(layoutInflater)

        initComponents()
    }

    private fun initComponents() {
        // Note that the Toolbar defined in the layout has the id "my_toolbar"
        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navBar
        val navController: NavController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        val isBottomView = setOf(
            R.id.calendar
        )

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

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