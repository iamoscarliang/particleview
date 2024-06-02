package com.oscarliang.particleview.core.util

import kotlin.random.Random

fun Random.nextIntSafely(from: Int, until: Int): Int {
    if (from > until) {
        throw IllegalArgumentException("Random range is empty: [$from, $until).")
    }
    return if (from == until) from else nextInt(from, until)
}

fun Random.nextFloatSafely(from: Float, until: Float): Float {
    if (from > until) {
        throw IllegalArgumentException("Random range is empty: [$from, $until).")
    }
    return if (from == until) from else from + (until - from) * nextFloat()
}