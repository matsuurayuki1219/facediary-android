package jp.matsuura.facediary.ui.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentSingupBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * サインアップ画面
 */

@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_singup) {

    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var binding: FragmentSingupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()
        initHandler()

    }

    private fun initListener() {
        binding.singUpButton.setOnClickListener {
            viewModel.onClickSignUpButton(
                userId = binding.userNameField.toString(),
                password = binding.passwordField.toString()
            )
        }
    }

    private fun initObserver() {
        viewModel.uiState.onEach {
            binding.progressBar.isVisible = it.isProgressVisible
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initHandler() {
        viewModel.event.onEach {
            when (it) {
                SignUpViewModel.Event.CanSignUp -> {
                    val direction = SignUpFragmentDirections.navigateToSignUpSuccessFragment()
                    findNavController().navigate(direction)
                }
                SignUpViewModel.Event.NetworkError -> {}
                SignUpViewModel.Event.UnknownError -> {}
                SignUpViewModel.Event.HasSameEmail -> {}
                SignUpViewModel.Event.ValidationError -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }
}