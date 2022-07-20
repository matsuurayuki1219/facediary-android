package jp.matsuura.facediary.model

import java.io.File

data class Calendar(
    val year: String,
    val month: String,
    val infoList: List<Info>,
)

data class Info(
    val day: String,
    val time: String,
    val emotion: Emotion,
    val image: File,
    val thought: String,
)

data class Emotion(
    val anger: Double,
    val contempt: Double,
    val disgust: Double,
    val fear: Double,
    val happiness: Double,
    val neutral: Double,
    val sadness: Double,
    val surprise: Double,
)
