package jp.matsuura.facediary.ui.signUp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentSinginBinding
import jp.matsuura.facediary.databinding.FragmentSingupBinding
import jp.matsuura.facediary.databinding.FragmentSingupSuccessBinding
import jp.matsuura.facediary.ui.signUp.SignUpViewModel
import kotlinx.coroutines.flow.onEach

/**
 * サインアップ成功画面
 */

@AndroidEntryPoint
class SignUpSuccessFragment: Fragment(R.layout.fragment_singup_success) {

    private var _binding: FragmentSingupSuccessBinding? = null
    private val binding: FragmentSingupSuccessBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingupSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openMailButton.setOnClickListener {
            val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}