package jp.matsuura.facediary.common.extenstion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.FileDescriptor

fun Uri.toBitmap(context: Context): Bitmap? {
    val parcelFileDescriptor: ParcelFileDescriptor? =
        context.contentResolver.openFileDescriptor(this, "r")
    parcelFileDescriptor?:return null

    val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
    val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor.close()
    return image
}

