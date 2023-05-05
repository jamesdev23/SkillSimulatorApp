package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap

class Build{
    var id: Int = 0
    var name: String = ""
    var gameName: String = ""
    var jobClassName: String = ""
    var description: String = ""
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var skillBuild: ArrayList<Skill> = ArrayList()
    var skillBuildText: String = ""
}