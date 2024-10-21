package com.oscarliang.particleview.core

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcelable
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
class ParticleSystem(
    val images: List<Image>,
    val startX: FloatOffset,
    val startY: FloatOffset,
    val speed: FloatOffset,
    val accelX: FloatOffset,
    val accelY: FloatOffset,
    val angle: IntOffset,
    val rotation: IntOffset,
    val rotationSpeed: FloatOffset,
    val particleDuration: Long,
    val particleFadeOutDuration: Long,
    val particlePerSecond: Int,
    val duration: Long,
    val density: Float
) : Parcelable {

    var drawArea = IntOffset(0, 0)

    private val particles = mutableListOf<Particle>()
    private val particlesPool = mutableListOf<Particle>()
    private val particlesToRemove = mutableListOf<Particle>()

    private var currentTime = 0L

    fun update(currentMillis: Long, elapsedMillis: Long) {
        val durationPerParticle = particleDuration + particleFadeOutDuration
        if (currentMillis < duration - durationPerParticle) {
            val millisPerParticle = 1000L / particlePerSecond
            currentTime += elapsedMillis
            if (currentTime >= millisPerParticle) {
                particles.add(getOneParticle().apply { reset(drawArea) })
                currentTime = 0L
            }
        }

        particles.forEach { particle ->
            particle.update(elapsedMillis)
        }

        // Remove the particle after updating
        // to prevent index out of bounds, and
        // recycle the particles being removed
        particles.removeAll(particlesToRemove)
        particlesPool.addAll(particlesToRemove)
        particlesToRemove.clear()
    }

    fun draw(bitmapPool: BitmapPool, canvas: Canvas, paint: Paint) {
        particles.forEach { particle ->
            particle.draw(
                bitmapPool = bitmapPool,
                canvas = canvas,
                paint = paint
            )
        }
    }

    fun release() {
        particles.clear()
        particlesPool.clear()
        particlesToRemove.clear()
    }

    private fun getOneParticle() =
        if (particlesPool.isNotEmpty()) {
            particlesPool.removeAt(0)
        } else {
            Particle(
                image = images[Random.nextInt(images.size)],
                startX = startX,
                startY = startY,
                speedMin = speed.startValue,
                speedMax = speed.endValue,
                accelXMin = accelX.startValue,
                accelXMax = accelX.endValue,
                accelYMin = accelY.startValue,
                accelYMax = accelY.endValue,
                angleMin = angle.startValue,
                angleMax = angle.endValue,
                rotationMin = rotation.startValue,
                rotationMax = rotation.endValue,
                rotationSpeedMin = rotationSpeed.startValue,
                rotationSpeedMax = rotationSpeed.endValue,
                duration = particleDuration,
                fadeOutDuration = particleFadeOutDuration,
                onParticleEnd = { particlesToRemove.add(it) },
                density = density
            )
        }

}