package com.oscarliang.particleview.core.model

data class IntOffset(
    val startValue: Int,
    val endValue: Int
) {
    constructor(value: Int) : this(value, value)
}

data class FloatOffset(
    val startValue: Float,
    val endValue: Float
) {
    constructor(value: Float) : this(value, value)
}