package jp.matsuura.facediary.ui.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.ActivityBinding
import jp.matsuura.facediary.common.extenstion.hideKeyboard
import jp.matsuura.facediary.common.extenstion.showConfirm
import timber.log.Timber

@AndroidEntryPoint
class Activity : AppCompatActivity() {

    private lateinit var binding: ActivityBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onStart() {
        super.onStart()
        handleDynamicLink()
    }

    private fun init() {
        navController = (supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment)
            .navController
    }

    private fun handleDynamicLink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
            .addOnSuccessListener {
                val deepLink: Uri? = it?.link
                val path = deepLink?.path
                if (path == "/register") {
                    val token: String = deepLink.getQueryParameter("token") ?: return@addOnSuccessListener
                    navigateToVerifyEmail(token = token)
                } else if (path == "/reset") {
                    val token: String = deepLink.getQueryParameter("token") ?: return@addOnSuccessListener
                    val email: String = deepLink.getQueryParameter("email") ?: return@addOnSuccessListener
                    navigateToResetPassword(email = email, token = token)
                }
            }
            .addOnFailureListener {
                Timber.d(it)
            }
    }

    private fun navigateToVerifyEmail(token: String) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.verifyEmailFragment,
            bundleOf("token" to token)
        )
    }

    private fun navigateToResetPassword(email: String, token: String) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.changePasswordFragment,
            Bundle().apply {
                bundleOf("token" to token)
                bundleOf("email" to email)
            }
        )
    }

}