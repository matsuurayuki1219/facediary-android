package jp.matsuura.facediary.ui.calendar

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentCalendarDetailBinding
import kotlinx.coroutines.flow.onEach

class CalendarDetailFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCalendarDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarDetailBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initObserver()
        initHandler()
    }

    private fun initComponents() {

    }

    private fun initObserver() {

    }

    private fun initHandler() {

    }

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_App_BottomSheetDialog
    }

    companion object {
        const val TAG = "CalendarDetailFragment"
    }

}