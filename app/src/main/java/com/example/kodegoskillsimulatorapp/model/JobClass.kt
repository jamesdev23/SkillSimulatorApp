package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap

class JobClass {
    var name:String = ""
    var type:String = ""
    var img:Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var description: String = ""
    var id: Int = 0
    var skillCount: Int = 0
}