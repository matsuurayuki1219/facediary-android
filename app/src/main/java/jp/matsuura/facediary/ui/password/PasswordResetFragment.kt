package jp.matsuura.facediary.ui.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentPasswordResetBinding
import jp.matsuura.facediary.databinding.FragmentSingupBinding
import jp.matsuura.facediary.ui.signUp.SignUpViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PasswordResetFragment: Fragment(R.layout.fragment_password_reset) {

    private val viewModel: PasswordResetViewModel by viewModels()

    private lateinit var binding: FragmentPasswordResetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()
        initHandler()
    }

    private fun initListener() {
        binding.resetButton.setOnClickListener {
            viewModel.onClickResetButton(
                email = binding.userNameField.toString()
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
            when(it) {
                PasswordResetViewModel.Event.CanReset -> {}
                PasswordResetViewModel.Event.NetworkError -> {}
                PasswordResetViewModel.Event.ValidationError -> {}
                PasswordResetViewModel.Event.UnknownError -> {}
                PasswordResetViewModel.Event.NotExistAccount -> {}
            }
        }
    }

}