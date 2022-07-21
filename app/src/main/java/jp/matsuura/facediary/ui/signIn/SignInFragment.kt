package jp.matsuura.facediary.ui.signIn

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
import jp.matsuura.facediary.databinding.FragmentSinginBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * サインイン画面
 */

@AndroidEntryPoint
class SignInFragment: Fragment(R.layout.fragment_singin) {

    private lateinit var binding: FragmentSinginBinding

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSinginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
        initHandler()
    }

    private fun initListener() {

        binding.singInButton.setOnClickListener {
            val userId = binding.userNameField.toString()
            val password = binding.passwordField.toString()
            viewModel.onClickSignInButton(userId = userId, password = password)
        }

        binding.signUpButton.setOnClickListener {
            viewModel.onClickSignUpButton()
        }

        binding.forgetPasswordButton.setOnClickListener {
            viewModel.onClickForgetButton()
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
                SignInViewModel.Event.CanSignIn -> {
                    val direction = SignInFragmentDirections.navigateToConfirmRegisterFeelingFragment()
                    findNavController().navigate(direction)
                }
                SignInViewModel.Event.NotExistUser -> {

                }
                SignInViewModel.Event.WrongPassword -> {

                }
                SignInViewModel.Event.UnknownError -> {

                }
                SignInViewModel.Event.NetworkError -> {
                    // For Demo
                    val direction = SignInFragmentDirections.navigateToCalendarFragment()
                    findNavController().navigate(direction)
                }
                SignInViewModel.Event.SignUp -> {
                    val direction = SignInFragmentDirections.navigateToSignUpFragment()
                    findNavController().navigate(direction)
                }
                SignInViewModel.Event.ForgetPassword -> {
                    val direction = SignInFragmentDirections.navigateToPasswordResetFragment()
                    findNavController().navigate(direction)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}