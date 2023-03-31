package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap

class Skill {
    var id: Int = 0
    var jobClassName: String = ""
    var gameName: String = ""
    var name: String = "Skill Name"
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var description: String = "Description"
    var maxLevel: Int = 0
    var minLevel: Int = 0
    var currentLevel: Int = 0
    var skillType: String = "Skill Type"

}
