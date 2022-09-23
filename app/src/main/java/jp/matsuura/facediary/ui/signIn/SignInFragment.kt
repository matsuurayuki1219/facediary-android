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
import jp.matsuura.facediary.BuildConfig
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentSinginBinding
import jp.matsuura.facediary.extenstion.hideKeyboard
import jp.matsuura.facediary.extenstion.showMessage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * サインイン画面
 */

@AndroidEntryPoint
class SignInFragment: Fragment(R.layout.fragment_singin) {

    private var _binding: FragmentSinginBinding? = null
    private val binding: FragmentSinginBinding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSinginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
        initHandler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {

        binding.singInButton.setOnClickListener {
            val userId = binding.emailEditTextView.text.toString()
            val password = binding.passwordEditTextView.text.toString()
            viewModel.onClickSignInButton(email = userId, password = password)
        }

        binding.signUpButton.setOnClickListener {
            viewModel.onClickSignUpButton()
        }

        binding.forgetPasswordButton.setOnClickListener {
            viewModel.onClickForgetButton()
        }

        if (BuildConfig.DEBUG) {
            binding.emailEditTextView.setText("test1@example.com")
            binding.passwordEditTextView.setText("pass9999")
        }

        binding.root.setOnClickListener {
            requireActivity().hideKeyboard()
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
                SignInViewModel.Event.ValidationMailError -> {
                    context?.showMessage(
                        titleRes = R.string.validation_email_error_title,
                        messageRes = R.string.validation_email_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
                SignInViewModel.Event.ValidationPasswordError -> {
                    context?.showMessage(
                        titleRes = R.string.validation_password_error_title,
                        messageRes = R.string.validation_password_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
                SignInViewModel.Event.MailNotVerified -> {
                    context?.showMessage(
                        titleRes = R.string.mail_not_verified_error_title,
                        messageRes = R.string.mail_not_verified_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
                SignInViewModel.Event.CanSignIn -> {
                    val direction = SignInFragmentDirections.navigateToHomeFragment()
                    findNavController().navigate(direction)
                }
                SignInViewModel.Event.NotExistUser -> {
                    context?.showMessage(
                        titleRes = R.string.user_not_exist_error_title,
                        messageRes = R.string.user_not_exist_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
                SignInViewModel.Event.WrongPassword -> {
                    context?.showMessage(
                        titleRes = R.string.password_error_title,
                        messageRes = R.string.password_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
                SignInViewModel.Event.UnknownError -> {
                    context?.showMessage(
                        titleRes = R.string.other_error_title,
                        messageRes = R.string.other_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
                SignInViewModel.Event.NetworkError -> {
                    context?.showMessage(
                        titleRes = R.string.network_error_title,
                        messageRes = R.string.network_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
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