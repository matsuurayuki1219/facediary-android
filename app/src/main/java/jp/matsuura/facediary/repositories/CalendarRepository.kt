package jp.matsuura.facediary.repositories

import jp.matsuura.facediary.api.CalendarApi
import jp.matsuura.facediary.model.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val calendarApi: CalendarApi,
) {

    suspend fun getCalendarData(year: Int, month: Int): Calendar {
        return withContext(Dispatchers.IO) {
            calendarApi.getCalendarData(
                year = year,
                month = month
            )
        }
    }

}