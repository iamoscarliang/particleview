package com.oscarliang.particleview.core.model

/**
 * The image data of particle
 *
 * @param imageId - the resource id of the image
 * @param size - the size of the image display on screen in dp
 * @param tag - an optional tag to identify image type
 */
data class Image(
    val imageId: Int,
    val size: Int,
    val tag: String? = null
)