package jp.matsuura.facediary.ui.auth.change_password

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
import jp.matsuura.facediary.R
import jp.matsuura.facediary.common.extenstion.showConfirm
import jp.matsuura.facediary.common.extenstion.showMessage
import jp.matsuura.facediary.databinding.FragmentChangePasswordBinding
import jp.matsuura.facediary.enums.ChangePasswordError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordFragment: Fragment(R.layout.fragment_change_password) {

    private val viewModel: ChangePasswordViewModel by viewModels()

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding: FragmentChangePasswordBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
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
        binding.changeButton.setOnClickListener {
            viewModel.onClickChangeButton(
                password = binding.userNameField.toString()
            )
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
                is ChangePasswordViewModel.Event.Success -> {}
                is ChangePasswordViewModel.Event.Failure -> {
                    when (it.error) {
                        ChangePasswordError.EMAIL_FORMAT_ERROR -> {
                            requireContext().showMessage(
                                titleRes = R.string.validation_email_error_title,
                                messageRes = R.string.validation_email_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        ChangePasswordError.PASSWORD_FORMAT_ERROR -> {}
                        ChangePasswordError.PASSWORD_FORMAT_ERROR -> {
                            requireContext().showMessage(
                                titleRes = R.string.validation_password_error_title,
                                messageRes = R.string.validation_password_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        ChangePasswordError.USER_NOT_EXIST -> {
                            requireContext().showMessage(
                                titleRes = R.string.user_not_exist_error_title,
                                messageRes = R.string.user_not_exist_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                        ChangePasswordError.TOKEN_WRONG -> {
                            requireContext().showMessage(
                                titleRes = R.string.other_error_title,
                                messageRes = R.string.other_error_message,
                                onPositiveClick = { dialog ->
                                    dialog.dismiss()
                                }
                            )
                        }
                    }
                }
                is ChangePasswordViewModel.Event.NetworkError -> {
                    requireContext().showConfirm(
                        titleRes = R.string.network_error_title,
                        messageRes = R.string.network_error_message,
                        positiveButtonRes = R.string.retry,
                        negativeButtonRes = R.string.cancel,
                        onPositiveClick = { dialog ->
                            dialog.dismiss()
                            viewModel.onClickChangeButton(
                                password = binding.userNameField.toString()
                            )
                        },
                        onNegativeClick = { dialog ->
                            dialog.dismiss()
                        },
                        isCancel = false,
                    )
                }
                is ChangePasswordViewModel.Event.UnknownError -> {
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