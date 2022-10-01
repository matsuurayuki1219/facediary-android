package jp.matsuura.facediary.ui.auth.signIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.BuildConfig
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentSinginBinding
import jp.matsuura.facediary.common.extenstion.hideKeyboard
import jp.matsuura.facediary.common.extenstion.showMessage
import jp.matsuura.facediary.enums.LoginError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                initObserver(coroutineScope = this)
                initHandler(coroutineScope = this)
            }
        }

        if (BuildConfig.DEBUG) {
            binding.emailEditTextView.setText("test1@example.com")
            binding.passwordEditTextView.setText("pass9999")
        }

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
            val direction = SignInFragmentDirections.navigateToSignUpFragment()
            findNavController().navigate(direction)
        }
        binding.forgetPasswordButton.setOnClickListener {
            val direction = SignInFragmentDirections.navigateToPasswordResetFragment()
            findNavController().navigate(direction)
        }
        binding.root.setOnClickListener {
            requireActivity().hideKeyboard()
        }
    }

    private fun initObserver(coroutineScope: CoroutineScope) {
        viewModel.uiState.onEach {
            binding.progressBar.isVisible = it.isProgressVisible
        }.launchIn(coroutineScope)
    }

    private fun initHandler(coroutineScope: CoroutineScope) {
        viewModel.event.onEach {
            when (it) {
                is SignInViewModel.Event.Success -> {
                    val direction = SignInFragmentDirections.navigateToHomeFragment()
                    findNavController().navigate(direction)
                }
                is SignInViewModel.Event.Failure -> {
                    when (it.error) {
                        LoginError.EMAIL_FORMAT_ERROR -> {
                            requireContext().showMessage(
                                titleRes = R.string.validation_email_error_title,
                                messageRes = R.string.validation_email_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        LoginError.PASSWORD_FORMAT_ERROR -> {
                            requireContext().showMessage(
                                titleRes = R.string.validation_password_error_title,
                                messageRes = R.string.validation_password_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        LoginError.USER_NOT_EXIST -> {
                            requireContext().showMessage(
                                titleRes = R.string.user_not_exist_error_title,
                                messageRes = R.string.user_not_exist_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        LoginError.MAIL_NOT_VERIFIED -> {
                            requireContext().showMessage(
                                titleRes = R.string.mail_not_verified_error_title,
                                messageRes = R.string.mail_not_verified_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        LoginError.PASSWORD_WRONG -> {
                            requireContext().showMessage(
                                titleRes = R.string.password_error_title,
                                messageRes = R.string.password_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                    }
                }
                is SignInViewModel.Event.UnknownError -> {
                    requireContext().showMessage(
                        titleRes = R.string.other_error_title,
                        messageRes = R.string.other_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
                is SignInViewModel.Event.NetworkError -> {
                    requireContext().showMessage(
                        titleRes = R.string.network_error_title,
                        messageRes = R.string.network_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
            }
        }.launchIn(coroutineScope)
    }

}