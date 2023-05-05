package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class Game {
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}

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

class Build{
    var id: Int = 0
    var name: String = ""
    var gameName: String = ""
    var jobClassName: String = ""
    var description: String = ""
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var skillBuild: ArrayList<Skill> = ArrayList()
    private var skillBuildText: String = ""

    fun setBuildText(text: String){
        skillBuildText = text
    }

    fun getBuildText(): String{
        return skillBuildText
    }
}

