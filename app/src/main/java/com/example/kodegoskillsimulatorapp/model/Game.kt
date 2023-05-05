package com.example.kodegoskillsimulatorapp.model

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

class Game {
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}