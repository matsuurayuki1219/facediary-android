package jp.matsuura.facediary.common.extenstion

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.toBinary(): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}