package jp.matsuura.facediary.ui.auth.reset_password

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentPasswordResetSuccessBinding

@AndroidEntryPoint
class PasswordResetSuccessFragment: Fragment(R.layout.fragment_password_reset_success) {

    private var _binding: FragmentPasswordResetSuccessBinding? = null
    private val binding: FragmentPasswordResetSuccessBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.openMailButton.setOnClickListener {
            val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }

}