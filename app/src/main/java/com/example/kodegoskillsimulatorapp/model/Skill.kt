package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap

class Skill {
    var name: String = "Unknown"
    var level: Int = 0
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var id: Int = 0
    var jobClassSkillID: Int = 0
    var skillType1: String = "skillType1"
    var skillType2: String = "skillType2"
    var minLevel: Int = 0
    var currentLevel: Int = 0
}
