package jp.matsuura.facediary.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentRegisterFeelingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFeelingFragment: Fragment(R.layout.fragment_register_feeling) {

    private var _binding: FragmentRegisterFeelingBinding? = null
    private val binding: FragmentRegisterFeelingBinding get() = _binding!!

    private val viewModel: RegisterFeelingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterFeelingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                handleUiState(coroutineScope = this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.onRadioButtonClicked(isChecked = -1 != checkedId)
        }

        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onCheckBoxClicked(isChecked = isChecked)
        }

        binding.nextButton.setOnClickListener {
            val text =
                if (binding.checkBox.isChecked) {
                    ""
                } else {
                    val selectedId = binding.radioGroup.checkedRadioButtonId
                    binding.radioGroup.findViewById<RadioButton>(selectedId).text.toString()
                }
            findNavController().navigate(
                RegisterFeelingFragmentDirections.navigateToShootCameraFragment(
                    selectedItem = text,
                )
            )
        }
    }

    private fun handleUiState(coroutineScope: CoroutineScope) {
        viewModel.uiState.onEach {
            binding.progressBar.isVisible = it.isProgressVisible
            binding.nextButton.isEnabled = it.isButtonEnable
            binding.radioGroup.children.forEach { radioButton ->
                val radioButton = radioButton as RadioButton
                radioButton.isEnabled = it.isRadioButtonEnabled
                if (!it.isRadioButtonEnabled) {
                    radioButton.isChecked = false
                }
            }
        }.launchIn(coroutineScope)
    }

}