package jp.matsuura.facediary.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.matsuura.facediary.common.enum.Youbi
import jp.matsuura.facediary.extenstion.dayValue
import jp.matsuura.facediary.extenstion.monthValue
import jp.matsuura.facediary.extenstion.yearValue
import jp.matsuura.facediary.model.Calendar
import jp.matsuura.facediary.model.Emotion
import jp.matsuura.facediary.model.Info
import jp.matsuura.facediary.repositories.AuthRepository
import jp.matsuura.facediary.repositories.CalendarRepository
import jp.matsuura.facediary.common.util.CalendarUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val authRepository: AuthRepository,
): ViewModel() {

    private var currentYear = Date().yearValue
    private var currentMonth = Date().monthValue
    private val currentDay = Date().dayValue

    private var currentCalendarData: Calendar? = null

    private val YOUBI_ARR = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            isProgressVisible = false,
            title = currentYear.toString() + "年" + currentMonth.toString() + "月",
            calendarInfo = emptyList(),
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _detailUiState: MutableStateFlow<DetailUiState> = MutableStateFlow(
        DetailUiState(
            detailInfo = Info(
                day = "",
                time = "",
                emotion = Emotion(
                    anger = 0.0,
                    contempt = 0.0,
                    disgust = 0.0,
                    fear = 0.0,
                    happiness = 0.0,
                    neutral = 0.0,
                    sadness = 0.0,
                    surprise = 0.0,
                ),
                image = null,
                thought = "",
            )
        )
    )
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

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

    fun onDayButtonClicked(day: String) {

        viewModelScope.launch {
            // val calendarData: Calendar = checkNotNull(currentCalendarData) { "the value of currentCalendarData is not set"}
            val calendarData: Calendar = currentCalendarData ?: return@launch
            val infoList: List<Info> = calendarData.infoList
            if (infoList.isEmpty()) {
                _event.emit(Event.NotExistData)
            } else {
                val info = infoList.find { it.day == day }
                if (info == null) {
                    _event.emit(Event.NotExistData)
                    return@launch
                }
                _detailUiState.value = _detailUiState.value.copy(
                    detailInfo = info,
                )
            }
        }
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
                currentCalendarData = it
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isProgressVisible = false,
                )
                _event.emit(Event.NetworkError)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.logout()
            }.onSuccess {
                _event.emit(Event.Logout)
            }.onFailure {
                Timber.d(it)
                _event.emit(Event.UnknownError)
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

    data class DetailUiState(
        val detailInfo: Info,
    )

    sealed class Event {
        object UnknownError: Event()
        object NetworkError: Event()
        object NotExistData: Event()
        object Logout: Event()
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