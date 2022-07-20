package jp.matsuura.facediary.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.enums.Youbi
import jp.matsuura.facediary.extenstions.dayValue
import jp.matsuura.facediary.extenstions.monthValue
import jp.matsuura.facediary.extenstions.yearValue
import jp.matsuura.facediary.model.Calendar
import jp.matsuura.facediary.repositories.CalendarRepository
import jp.matsuura.facediary.utils.CalendarUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
): ViewModel() {

    private var currentYear = Date().yearValue
    private var currentMonth = Date().monthValue
    private val currentDay = Date().dayValue

    private var currentCalendarData: List<Calendar> = emptyList()

    private val YOUBI_ARR = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
            title = currentYear.toString() + "年" + currentMonth.toString() + "月",
            calendarInfo = emptyList(),
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    init {
        createCalendarInfo()
        getCalendarData()
    }


    fun onNextButtonClicked() {

        if (currentMonth == 12) {
            currentYear += 1
            currentMonth = 1
        } else {
            currentMonth += 1
        }
        _uiState.value = _uiState.value.copy(
            title = currentYear.toString() + "年" + currentMonth.toString() + "月"
        )

        createCalendarInfo()
        getCalendarData()

    }

    fun onPrevButtonClicked() {

        if (currentMonth == 1) {
            currentYear -= 1
            currentMonth = 12
        } else {
            currentMonth -= 1
        }

        _uiState.value = _uiState.value.copy(
            title = currentYear.toString() + "年" + currentMonth.toString() + "月"
        )

        createCalendarInfo()
        getCalendarData()

    }

    fun onDayButtonClicked() {
        
    }


    private fun getCalendarData() {
        viewModelScope.launch {
            kotlin.runCatching {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = true,
                )
                calendarRepository.getCalendarData(
                    year = currentYear,
                    month = currentMonth
                )
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                _event.emit(Event.NetworkError)
            }
        }
    }

    private fun createCalendarInfo() {

        val calendarList = mutableListOf<CalendarItem>()
        YOUBI_ARR.forEach {
            calendarList.add(CalendarItem.Header(it))
        }

        val currentYear = currentYear
        val currentMonth = currentMonth

        val days = CalendarUtil.numberOfDays(
            year = currentYear,
            month = currentMonth
        )

        val firstOfWeek = CalendarUtil.dayOfWeek(
            year = currentYear,
            month = currentMonth,
            day = 1
        )

        val prevDays = firstOfWeek.value

        val prevMonthDays = if (currentMonth == 1) {
            CalendarUtil.numberOfDays(year = currentYear - 1, month = 12)
        } else {
            CalendarUtil.numberOfDays(year = currentYear, month = currentMonth - 1)
        }

        for (i in 1..prevDays) {
            calendarList.add(CalendarItem.Day(day = prevMonthDays - prevDays + i))
        }

        for (i in 1..days) {
            calendarList.add(
                CalendarItem.Day(
                    day = i,
                    isToDay = i == currentDay,
                    isCurrentMonth = true,
                    youbi = CalendarUtil.dayOfWeek(year = currentYear, month = currentMonth, day = i)
                )
            )
        }

        val nextDays = 42 - days - prevDays

        for (i in 1..nextDays) {
            calendarList.add(CalendarItem.Day(i))
        }

        _uiState.value = _uiState.value.copy(
            calendarInfo = calendarList.toList(),
        )

    }

    data class UiState(
        val isProgressVisible: Boolean,
        val title: String,
        val calendarInfo: List<CalendarItem>,
    )

    sealed class Event {
        object UnknownError: Event()
        object NetworkError: Event()
    }

    sealed class CalendarItem {

        data class Header(
            val youbi: String
            ) : CalendarItem()

        data class Day(
            val day: Int,
            val isToDay: Boolean = false,
            val isCurrentMonth: Boolean = false,
            val youbi: Youbi? = null,
            ) : CalendarItem()
    }

}