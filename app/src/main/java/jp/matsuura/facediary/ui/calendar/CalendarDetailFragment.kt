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

@AndroidEntryPoint
class CalendarDetailFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCalendarDetailBinding

    private val viewModel by hiltNavGraphViewModels<CalendarViewModel>(R.id.calendar_nav)

    private val args: CalendarDetailFragmentArgs by navArgs()

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
        viewModel.onDayButtonClicked(args.day.toString())
    }

    private fun initObserver() {
        viewModel.detailUiState.onEach {
            binding.time.text = it.detailInfo.day + " " + it.detailInfo.time
            binding.thought.text = it.detailInfo.thought
            binding.anger.progress = it.detailInfo.emotion.anger.toInt()
            binding.contempt.progress = it.detailInfo.emotion.contempt.toInt()
            binding.disgust.progress = it.detailInfo.emotion.disgust.toInt()
            binding.happiness.progress = it.detailInfo.emotion.happiness.toInt()
            binding.fear.progress = it.detailInfo.emotion.fear.toInt()
            binding.neutral.progress = it.detailInfo.emotion.neutral.toInt()
            binding.sadness.progress = it.detailInfo.emotion.sadness.toInt()
            binding.surprise.progress = it.detailInfo.emotion.surprise.toInt()
        }
    }

    private fun initHandler() {
        viewModel.event.onEach {
            when (it) {
                CalendarViewModel.Event.NotExistData -> {}
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_App_BottomSheetDialog
    }

    companion object {
        const val TAG = "CalendarDetailFragment"
    }

}