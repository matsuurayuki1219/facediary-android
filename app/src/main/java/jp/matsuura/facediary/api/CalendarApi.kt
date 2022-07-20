package jp.matsuura.facediary.api

import jp.matsuura.facediary.model.Calendar
import retrofit2.http.GET

interface CalendarApi {

    @GET("/v1/calendar/detail")
    suspend fun getCalendarData(year: Int, month: Int): Calendar

}