package jp.matsuura.facediary.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentHomeBinding
import jp.matsuura.facediary.extenstion.showConfirm

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun initListener() {
        val navController = (childFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment)
            .navController
        binding.navBar.setupWithNavController(navController)

        binding.fabButton.setOnClickListener {
            showConfirmMessage()
        }
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