package jp.matsuura.facediary.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                handleEvent(coroutineScope = this)
            }
        }
    }

    private fun handleEvent(coroutineScope: CoroutineScope) {
        viewModel.event.onEach {
            val directions = when (it) {
                SplashViewModel.Event.IsLogin -> {
                    SplashFragmentDirections.navigateToHomeFragment()
                }
                SplashViewModel.Event.IsNotLogin -> {
                    SplashFragmentDirections.navigateToSignInFragment()
                }
            }
            findNavController().navigate(directions)
        }.launchIn(coroutineScope)
    }
}