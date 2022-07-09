package jp.matsuura.facediary.ui.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentSingupBinding
import jp.matsuura.facediary.ui.signUp.SignUpViewModel

/**
 * サインアップ画面
 */

@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_singup) {

    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var binding: FragmentSingupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSingupBinding.inflate(layoutInflater)
    }
}