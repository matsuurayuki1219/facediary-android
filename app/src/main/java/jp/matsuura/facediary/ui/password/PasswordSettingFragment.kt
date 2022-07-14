package jp.matsuura.facediary.ui.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentPasswordSettingBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PasswordSettingFragment: Fragment(R.layout.fragment_password_setting) {

    private val viewModel: PasswordSettingViewModel by viewModels()

    private lateinit var binding: FragmentPasswordSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()
        initHandler()
    }

    private fun initListener() {
        binding.changeButton.setOnClickListener {
            viewModel.onClickChangeButton(
                userId = binding.userNameField.toString()
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
                PasswordSettingViewModel.Event.CanChange -> {}
                PasswordSettingViewModel.Event.NetworkError -> {}
                PasswordSettingViewModel.Event.UnknownError -> {}
                PasswordSettingViewModel.Event.ValidationError -> {}
            }
        }
    }

}