package com.example.for_testing.model

import android.graphics.Bitmap

class Game {
    var id: Int = 0
    var name: String = "Game name"
    var description: String = "Game Description"
    var icon: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}