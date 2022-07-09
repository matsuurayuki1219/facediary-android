package jp.matsuura.facediary.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * スプラッシュ画面
 */

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        viewModel.event.onEach {
            val directions = when (it) {
                SplashViewModel.Event.IsLogin -> {
                    SplashFragmentDirections.navigateToConfirmRegisterFeelingFragment()
                }
                SplashViewModel.Event.IsNotLogin -> {
                    SplashFragmentDirections.navigateToSignInFragment()
                }
            }
            findNavController().navigate(directions)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}