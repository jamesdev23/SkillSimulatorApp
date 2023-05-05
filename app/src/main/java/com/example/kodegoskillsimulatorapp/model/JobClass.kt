package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap

class JobClass {
    var id: Int = 0
    var name:String = ""
    var gameName: String = ""
    var maxSkillPoints: Int = 50
    var jobClassType:String = ""
    var description: String = ""
    var img:Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

}