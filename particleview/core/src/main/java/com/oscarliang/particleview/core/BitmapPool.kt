package com.oscarliang.particleview.core

import android.graphics.Bitmap

class BitmapPool {

    private val bitmaps = mutableMapOf<Int, Bitmap>()

    fun put(bitmap: Bitmap, id: Int) {
        bitmaps[id] = bitmap
    }

    fun get(id: Int): Bitmap? = bitmaps[id]

}