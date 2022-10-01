package jp.matsuura.facediary.ui.auth.reset_password

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
import jp.matsuura.facediary.databinding.FragmentPasswordResetBinding
import jp.matsuura.facediary.common.extenstion.showConfirm
import jp.matsuura.facediary.common.extenstion.showMessage
import jp.matsuura.facediary.enums.ResetPasswordError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordResetFragment: Fragment(R.layout.fragment_password_reset) {

    private val viewModel: PasswordResetViewModel by viewModels()

    private var _binding: FragmentPasswordResetBinding? = null
    private val binding: FragmentPasswordResetBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
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
        binding.resetButton.setOnClickListener {
            viewModel.onClickResetButton(
                email = binding.emailEditTextView.text.toString(),
            )
        }

        if (BuildConfig.DEBUG) {
            binding.emailEditTextView.setText("yuki.matsuura@progrit.co.jp")
        }
    }

    private fun initObserver(coroutineScope: CoroutineScope) {
        viewModel.uiState.onEach {
            binding.progressBar.isVisible = it.isProgressVisible
        }.launchIn(coroutineScope)
    }

    private fun initHandler(coroutineScope: CoroutineScope) {
        viewModel.event.onEach {
            when(it) {
                is PasswordResetViewModel.Event.Success -> {
                    findNavController().navigate(PasswordResetFragmentDirections.navigateToPasswordResetSuccessFragment())
                }
                is PasswordResetViewModel.Event.Failure -> {
                    when (it.error) {
                        ResetPasswordError.EMAIL_FORMAT_ERROR -> {
                            requireContext().showMessage(
                                titleRes = R.string.validation_email_error_title,
                                messageRes = R.string.validation_email_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        ResetPasswordError.USER_NOT_EXIST -> {
                            requireContext().showMessage(
                                titleRes = R.string.user_not_exist_error_title,
                                messageRes = R.string.user_not_exist_error_message,
                                positiveButtonRes = R.string.ok,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                    findNavController().popBackStack(R.id.signInFragment, false)
                                },
                                isCancel = false,
                            )
                        }
                        else -> {
                            requireContext().showMessage(
                                titleRes = R.string.other_error_title,
                                messageRes = R.string.other_error_message,
                                positiveButtonRes = R.string.ok,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                    findNavController().popBackStack(R.id.signInFragment, false)
                                },
                                isCancel = false,
                            )
                        }
                    }
                }
                is PasswordResetViewModel.Event.NetworkError -> {
                    requireContext().showConfirm(
                        titleRes = R.string.network_error_title,
                        messageRes = R.string.network_error_message,
                        positiveButtonRes = R.string.retry,
                        negativeButtonRes = R.string.cancel,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                            viewModel.onClickResetButton(
                                email = binding.emailEditTextView.text.toString(),
                            )
                        },
                        onNegativeClick = { dialog ->
                            dialog.dismiss()
                        },
                        isCancel = false,
                    )
                }
                is PasswordResetViewModel.Event.UnknownError -> {
                    requireContext().showMessage(
                        titleRes = R.string.other_error_title,
                        messageRes = R.string.other_error_message,
                        positiveButtonRes = R.string.ok,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                            findNavController().popBackStack(R.id.signInFragment, false)
                        },
                        isCancel = false,
                    )
                }

            }
        }.launchIn(coroutineScope)
    }

}