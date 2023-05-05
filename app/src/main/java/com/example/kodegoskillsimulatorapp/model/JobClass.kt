package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap

class JobClass {
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var gameName: String = ""
    var jobClassType: String = ""
    private var maxSkillPoints: Int = 50

    fun setMaxSkillPoints(skillPoints: Int){
        maxSkillPoints = skillPoints
    }

    fun getMaxSkillPoints(): Int{
        return maxSkillPoints
    }
}