package com.example.for_testing.model

import android.graphics.Bitmap

class JobClass {
    var id: Int = 0
    var gameId: Int = 0
    var name:String = ""
    var type:String = ""
    var description: String = ""
    var img:Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

}