package jp.matsuura.facediary.common.extenstion

import java.util.*

/**
 * DateからCalendarを取得します。
 */
val Date.calendar: java.util.Calendar
    get() {
        val calendar = java.util.Calendar.getInstance()
        calendar.time = this
        return calendar
    }

/**
 * Dateの年の値を取得します。
 */
val Date.yearValue: Int
    get() {
        return this.calendar.get(java.util.Calendar.YEAR)
    }

/**
 * Dateの月の値を取得します。
 */
val Date.monthValue: Int
    get() {
        return this.calendar.get(java.util.Calendar.MONTH) + 1
    }

/**
 * Dateの日の値を取得します。
 */
val Date.dayValue: Int
    get() {
        return this.calendar.get(java.util.Calendar.DAY_OF_MONTH)
    }

/**
 * Dateの時の値を取得します。
 */
val Date.hourValue: Int
    get() {
        return this.calendar.get(java.util.Calendar.HOUR_OF_DAY)
    }

/**
 * Dateの分の値を取得します。
 */
val Date.minuteValue: Int
    get() {
        return this.calendar.get(java.util.Calendar.MINUTE)
    }

/**
 * Dateの秒の値を取得します。
 */
val Date.secondValue: Int
    get() {
        return this.calendar.get(java.util.Calendar.SECOND)
    }

/**
 * Dateのミリ秒の値を取得します。
 */
val Date.millisecondValue: Int
    get() {
        return this.calendar.get(java.util.Calendar.MILLISECOND)
    }


