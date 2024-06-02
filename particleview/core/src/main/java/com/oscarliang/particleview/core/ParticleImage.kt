package com.oscarliang.particleview.core

import android.graphics.Bitmap

/**
 * The low level image data of particle storing the actual bitmap render on screen
 *
 * @param bitmap - the bitmap of the image to be render on screen
 * @param size - the size of the image display on screen in dp
 * @param tag - an optional tag to identify image type
 */
data class ParticleImage(
    val bitmap: Bitmap,
    val size: Int,
    val tag: String? = null
) {
    val ratio: Float = bitmap.height * 1.0f / bitmap.width
}