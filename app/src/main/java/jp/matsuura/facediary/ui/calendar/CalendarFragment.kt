package jp.matsuura.facediary.ui.calendar

import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentCalendarBinding
import jp.matsuura.facediary.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import kotlin.math.abs

@AndroidEntryPoint
class CalendarFragment: Fragment(R.layout.fragment_calendar) {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by viewModels()

    private lateinit var adapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initListener()
        initObserver()
        initHandler()
    }

    private fun initComponents() {
        adapter = CalendarAdapter(onItemClick = { day ->
            // Note: 数字を押下した際の処理を実装します
        })
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = GridLayoutManager(context, 7)
    }

    private fun initListener() {
        binding.nextButton.setOnClickListener {
            viewModel.onNextButtonClicked()
        }
        binding.backButton.setOnClickListener {
            viewModel.onPrevButtonClicked()
        }
    }

    private fun initObserver() {
        viewModel.uiState.onEach {
            binding.progressBar.isVisible = it.isProgressVisible
            adapter.dataSource = it.calendarInfo
            (activity as AppCompatActivity).supportActionBar?.title = it.title
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initHandler() {
        viewModel.event.onEach {
            when (it) {
                CalendarViewModel.Event.NetworkError -> {}
                CalendarViewModel.Event.UnknownError -> {}
                CalendarViewModel.Event.Logout -> {}
                else -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}