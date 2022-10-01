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
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentChangePasswordBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
        initObserver()
        initHandler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.changeButton.setOnClickListener {
            viewModel.onClickChangeButton(
                userId = binding.userNameField.toString()
            )
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
                        ChangePasswordViewModel.Event.CanChange -> {}
                        ChangePasswordViewModel.Event.NetworkError -> {}
                        ChangePasswordViewModel.Event.UnknownError -> {}
                        ChangePasswordViewModel.Event.ValidationPasswordError -> {}
                        ChangePasswordViewModel.Event.NotExistUser -> {}
                    }
                }
            }
        }
    }

}