package com.oscarliang.particleview.core

import android.os.Parcelable
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset
import kotlinx.parcelize.Parcelize

/**
 *  Data class to store all the configurations of the animation
 *
 * @param images - the image of the particle
 * @param startX - the start x position of the particle. Range from [0.0f, 1.0f] relative to the view's width
 * @param startY - the start y position of the particle. Range from [0.0f, 1.0f] relative to the view's height
 * @param speed - the speed of the particle moving in (px / per second)
 * @param accelX - the x acceleration added to the speed per second
 * @param accelY - the y acceleration added to the speed per second
 * @param angle - the direction of the particle's speed. Range from [0, 360].
 * Top - 180, Right - 90, Bottom - 0, Left - 270
 * @param rotation - the start rotation of the particle. Range from [0, 360]
 * @param rotationSpeed - the rotation speed of the particle rotating in (degree / per second)
 * @param particleDuration - the duration of the individual particle
 * @param particleFadeOutDuration - the duration of the fade out effect of particle
 * @param particlePerSecond - the amount of particle being emitted per second
 */
@Parcelize
data class ParticleConfig(
    val images: List<Image>,
    val startX: FloatOffset = FloatOffset(0.0f, 1.0f),
    val startY: FloatOffset = FloatOffset(0.0f, 1.0f),
    val speed: FloatOffset = FloatOffset(500.0f, 1000.0f),
    val accelX: FloatOffset = FloatOffset(0.0f),
    val accelY: FloatOffset = FloatOffset(0.0f),
    val angle: IntOffset = IntOffset(0, 360),
    val rotation: IntOffset = IntOffset(0, 360),
    val rotationSpeed: FloatOffset = FloatOffset(0.0f),
    val particleDuration: Long = 1500L,
    val particleFadeOutDuration: Long = 1500L,
    val particlePerSecond: Int = 5
) : Parcelable