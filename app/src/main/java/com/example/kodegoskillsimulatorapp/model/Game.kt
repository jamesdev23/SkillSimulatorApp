package com.example.kodegoskillsimulatorapp.model

import android.graphics.Bitmap
import com.example.kodegoskillsimulatorapp.R

class Game {
    var id: Int = 0
    var name: String = "Game name"
    var description: String = "Game Description"
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}