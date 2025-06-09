package com.adama.quicksnap.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.net.toUri
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    fun saveImageToGallery(context: Context, photoUriString: String) {
        val photoUri = photoUriString.toUri()
        val inputStream = context.contentResolver.openInputStream(photoUri)
        val filename = "QuickSnap_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/QuickSnap")
            }
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null && inputStream != null) {
            resolver.openOutputStream(uri).use { outputStream ->
                inputStream.copyTo(outputStream!!)
            }
            Toast.makeText(context, "Saved to gallery!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}
