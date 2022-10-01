package jp.matsuura.facediary.ui.auth.signUp

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
import jp.matsuura.facediary.databinding.FragmentSingupBinding
import jp.matsuura.facediary.common.extenstion.hideKeyboard
import jp.matsuura.facediary.common.extenstion.showMessage
import jp.matsuura.facediary.enums.CreateUserError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_singup) {

    private val viewModel: SignUpViewModel by viewModels()

    private var _binding: FragmentSingupBinding? = null
    private val binding: FragmentSingupBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingupBinding.inflate(inflater, container, false)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.singUpButton.setOnClickListener {
            viewModel.onClickSignUpButton(
                email = binding.emailEditTextView.text.toString(),
                password = binding.passwordEditTextView.text.toString(),
            )
        }

        binding.root.setOnClickListener {
            requireActivity().hideKeyboard()
        }

        if (BuildConfig.DEBUG) {
            binding.emailEditTextView.setText("yuki.matsuura@progrit.co.jp")
            binding.passwordEditTextView.setText("pass9999")
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
                is SignUpViewModel.Event.Success -> {
                    val direction = SignUpFragmentDirections.navigateToSignUpSuccessFragment()
                    findNavController().navigate(direction)
                }
                is SignUpViewModel.Event.Failure -> {
                    when (it.error) {
                        CreateUserError.EMAIL_FORMAT_ERROR -> {
                            requireContext().showMessage(
                                titleRes = R.string.validation_email_error_title,
                                messageRes = R.string.validation_email_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        CreateUserError.PASSWORD_FORMAT_ERROR -> {
                            requireContext().showMessage(
                                titleRes = R.string.validation_password_error_title,
                                messageRes = R.string.validation_password_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        CreateUserError.USER_ALREADY_EXIST -> {
                            requireContext().showMessage(
                                titleRes = R.string.user_already_existed_error_title,
                                messageRes = R.string.user_already_existed_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                    }
                }
                is SignUpViewModel.Event.UnknownError -> {
                    requireContext().showMessage(
                        titleRes = R.string.other_error_title,
                        messageRes = R.string.other_error_message,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                        }
                    )
                }
                is SignUpViewModel.Event.NetworkError -> {
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