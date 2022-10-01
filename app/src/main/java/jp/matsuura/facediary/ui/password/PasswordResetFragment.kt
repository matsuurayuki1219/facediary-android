package jp.matsuura.facediary.ui.password

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
import jp.matsuura.facediary.databinding.FragmentSingupBinding
import jp.matsuura.facediary.common.extenstion.showConfirm
import jp.matsuura.facediary.common.extenstion.showMessage
import jp.matsuura.facediary.ui.signUp.SignUpViewModel
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
        initObserver()
        initHandler()
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

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach {
                    binding.progressBar.isVisible = it.isProgressVisible
                }.launchIn(this)
            }
        }
    }

    private fun initHandler() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.onEach {
                    when(it) {
                        PasswordResetViewModel.Event.CanReset -> {
                            findNavController().navigate(PasswordResetFragmentDirections.navigateToPasswordResetSuccessFragment())
                        }
                        PasswordResetViewModel.Event.NetworkError -> {
                            showNetworkErrorMessage()
                        }
                        PasswordResetViewModel.Event.ValidationMailError -> {
                            showValidationErrorMessage()
                        }
                        PasswordResetViewModel.Event.UnknownError -> {
                            showUnknownErrorMessage()
                        }
                        PasswordResetViewModel.Event.NotExistAccount -> {
                            showErrorMessage()
                        }
                    }
                }.launchIn(this)
            }
        }
    }

    private fun showErrorMessage() {
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

    private fun showUnknownErrorMessage() {
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

    private fun showValidationErrorMessage() {
        requireContext().showMessage(
            titleRes = R.string.validation_email_error_title,
            messageRes = R.string.validation_email_error_message,
            onPositiveClick = { dialog ->
                dialog.dismiss()
            }
        )
    }

    private fun showNetworkErrorMessage() {
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

}