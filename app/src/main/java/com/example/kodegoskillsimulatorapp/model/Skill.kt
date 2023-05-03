package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Skill(
    var id: Int,
    var name: String,
    var jobClassName: String,
    var gameName: String,
    var icon: Bitmap,
    var description: String,
    var maxLevel: Int,
    var minLevel: Int,
    var currentLevel: Int,
    var skillType: String
): Parcelable