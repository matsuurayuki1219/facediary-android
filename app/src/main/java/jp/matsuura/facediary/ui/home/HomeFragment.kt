package jp.matsuura.facediary.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentHomeBinding
import jp.matsuura.facediary.common.extenstion.showConfirm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                handleEvent(coroutineScope = this)
            }
        }
    }

    private fun initListener() {
        val navController = (childFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment)
            .navController
        binding.navBar.setupWithNavController(navController)

        binding.fabButton.setOnClickListener {
            showConfirmMessage()
        }

        binding.appBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    viewModel.onLogoutButtonClicked()
                    true
                }
                else -> false
            }
        }
    }

    private fun handleEvent(coroutineScope: CoroutineScope) {
        viewModel.event.onEach {
            when (it) {
                is HomeViewModel.Event.Logout -> {
                    findNavController().navigate(HomeFragmentDirections.navigateToSignInFragment())
                }
            }
        }.launchIn(coroutineScope)
    }

    private fun showConfirmMessage() {
        requireContext().showConfirm(
            titleRes = R.string.dummy,
            messageRes = R.string.dummy,
            positiveButtonRes = R.string.dummy,
            negativeButtonRes = R.string.dummy,
            onPositiveClick = { alertDialog ->
                alertDialog.dismiss()
            },
            onNegativeClick = { alertDialog ->
                alertDialog.dismiss()
            },
            isCancel = false,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}