package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap

class Build {
    var id: Int = 0
    var gameName: String = ""
    var jobClassName: String = ""
    var name: String = ""
    var description: String = ""
    var dataText: String = ""
    var img: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var isFavorite: Boolean = false

}