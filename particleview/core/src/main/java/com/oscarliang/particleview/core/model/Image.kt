package com.oscarliang.particleview.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val imageId: Int,
    val size: Int
) : Parcelable