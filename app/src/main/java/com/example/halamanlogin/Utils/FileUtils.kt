package com.example.halamanlogin.Utils

import android.content.Context
import android.net.Uri

object FileUtils {
    fun getPath(context: Context, uri: Uri): String? {
        val projection = arrayOf(android.provider.MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        return cursor?.let {
            val columnIndex = it.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA)
            it.moveToFirst()
            val path = it.getString(columnIndex)
            it.close()
            path
        }
    }
}
