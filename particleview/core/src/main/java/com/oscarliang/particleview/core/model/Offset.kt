package com.oscarliang.particleview.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IntOffset(
    val startValue: Int,
    val endValue: Int
) : Parcelable {

    constructor(value: Int) : this(value, value)

}

@Parcelize
data class FloatOffset(
    val startValue: Float,
    val endValue: Float
) : Parcelable {

    constructor(value: Float) : this(value, value)

}