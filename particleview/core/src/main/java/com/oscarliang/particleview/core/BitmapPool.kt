package com.oscarliang.particleview.core

import android.graphics.Bitmap

class BitmapPool {

    private val bitmaps = mutableMapOf<Int, Bitmap>()

    fun put(image: Bitmap, id: Int) {
        bitmaps[id] = image
    }

    fun get(id: Int): Bitmap? = bitmaps[id]

}