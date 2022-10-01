package jp.matsuura.facediary.ui.verify

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
import jp.matsuura.facediary.databinding.FragmentVerifyEmailBinding
import jp.matsuura.facediary.common.extenstion.showConfirm
import jp.matsuura.facediary.common.extenstion.showMessage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerifyEmailFragment: Fragment(R.layout.fragment_verify_email) {

    private var _binding: FragmentVerifyEmailBinding? = null
    private val binding: FragmentVerifyEmailBinding get() = _binding!!

    private val viewModel: VerifyEmailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerifyEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUi()
        handleEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach {
                    binding.progressBar.isVisible = it.isProgressVisible
                }.launchIn(this)
            }
        }
    }

    private fun handleEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.onEach {
                    when (it) {
                        is VerifyEmailViewModel.Event.Success -> {
                            showSuccessMessage()
                        }
                        is VerifyEmailViewModel.Event.NotVerifyToken -> {
                            showErrorMessage()
                        }
                        is VerifyEmailViewModel.Event.NetworkError -> {
                            showNetworkErrorMessage()
                        }
                        is VerifyEmailViewModel.Event.UnknownError -> {
                            showUnknownErrorMessage()
                        }
                    }
                }.launchIn(this)
            }
        }
    }

    private fun showSuccessMessage() {
        requireContext().showMessage(
            titleRes = R.string.dummy,
            messageRes = R.string.dummy,
            positiveButtonRes = R.string.dummy,
            onPositiveClick = { dialog ->
                dialog.dismiss()
                findNavController().navigate(VerifyEmailFragmentDirections.navigateToHomeFragment())
            },
            isCancel = false,
        )
    }

    private fun showErrorMessage() {
        requireContext().showMessage(
            titleRes = R.string.verify_email_dialog_title_not_verify_token,
            messageRes = R.string.verify_email_dialog_message_not_verify_token,
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

    private fun showNetworkErrorMessage() {
        requireContext().showConfirm(
            titleRes = R.string.network_error_title,
            messageRes = R.string.network_error_message,
            positiveButtonRes = R.string.retry,
            negativeButtonRes = R.string.cancel,
            onPositiveClick = { dialog ->
                dialog.dismiss()
                viewModel.checkVerifyToken()
            },
            onNegativeClick = { dialog ->
                dialog.dismiss()
            },
            isCancel = false,
        )
    }

}