package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap

class Skill {
    var id: Int = 0
    var jobClassName: String = ""
    var gameName: String = ""
    var name: String = ""
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var description: String = ""
    var maxLevel: Int = 10
    var minLevel: Int = 0
    var currentLevel: Int = 0
    var skillType: String = ""
    var requiredSkill: Boolean = false
    var requiredSkillName: String = ""
    var requiredSkillLevel: Int = 1
}
