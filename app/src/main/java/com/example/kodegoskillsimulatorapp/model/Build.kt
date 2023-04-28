package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap

class Build {
    var id: Int = 0
    var name: String = "Custom Build"
    var gameName: String = "Custom Game"
    var jobClassName: String = "Custom Class"
    var description: String = "No description set."
    var dataText: String = ""
    var skillList: ArrayList<Skill> = arrayListOf()
    var img: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var isFavorite: Boolean = false

}