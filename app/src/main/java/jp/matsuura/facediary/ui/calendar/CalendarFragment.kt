package jp.matsuura.facediary.ui.calendar

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.FragmentCalendarBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import kotlin.math.abs

@AndroidEntryPoint
class CalendarFragment: Fragment(R.layout.fragment_calendar) {

    private lateinit var binding: FragmentCalendarBinding

    private val viewModel by hiltNavGraphViewModels<CalendarViewModel>(R.id.calendar_nav)

    private lateinit var adapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
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
            // show detail
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
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}