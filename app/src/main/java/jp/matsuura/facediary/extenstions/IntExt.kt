package jp.matsuura.facediary.extenstions

import android.content.Context

fun Int.dpToPx(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return (this * metrics.density).toInt()
}

fun Int.pxToDp(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return (this / metrics.density).toInt()
}