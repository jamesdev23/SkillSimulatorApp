package com.example.kodegoskillsimulatorapp.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream

class ImageUploadUtility {

    companion object {
        private const val ICON_NAME = "icon"

        fun uploadAndSaveIcon(context: Context, imageUri: Uri): String {
            var result: String = ""

            val contentResolver = context.contentResolver

            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, ICON_NAME)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }

            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outputStream ->
                    contentResolver.openInputStream(imageUri)?.copyTo(outputStream!!)
                }

                // Convert the image to a Base64-encoded string and save it to the game's icon parameter
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imageBytes = outputStream.toByteArray()
                result = Base64.encodeToString(imageBytes, Base64.DEFAULT)

                // Release the bitmap resources
                bitmap.recycle()

            }
            return result
        }
    }
}