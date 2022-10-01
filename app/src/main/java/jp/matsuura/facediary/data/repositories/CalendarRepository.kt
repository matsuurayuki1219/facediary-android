package jp.matsuura.facediary.data.repositories

import jp.matsuura.facediary.data.api.CalendarApi
import jp.matsuura.facediary.data.model.Calendar
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