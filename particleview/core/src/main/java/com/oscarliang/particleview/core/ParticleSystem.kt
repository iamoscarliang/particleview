package com.oscarliang.particleview.core

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcelable
import com.oscarliang.particleview.core.model.IntOffset
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

/**
 *  Parent class to initialize and update the particles
 *
 * @param config - the configuration to be applied to the particle
 */
@Parcelize
class ParticleSystem(
    val config: ParticleConfig
) : Parcelable {

    var drawArea = IntOffset(0, 0)

    private val particles = mutableListOf<Particle>()
    private val particlesPool = mutableListOf<Particle>()
    private val particlesToRemove = mutableListOf<Particle>()

    private var currentTime = 0L

    fun update(
        currentMillis: Long,
        elapsedMillis: Long,
        duration: Long
    ) {
        val durationPerParticle = config.particleDuration + config.particleFadeOutDuration
        if (currentMillis < duration - durationPerParticle) {
            val millisPerParticle = 1000L / config.particlePerSecond
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

    fun draw(
        bitmapPool: BitmapPool,
        canvas: Canvas,
        paint: Paint,
        density: Float
    ) {
        particles.forEach { particle ->
            particle.draw(
                bitmapPool = bitmapPool,
                canvas = canvas,
                paint = paint,
                density = density
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
                image = config.images[Random.nextInt(config.images.size)],
                startX = config.startX,
                startY = config.startY,
                speedMin = config.speed.startValue,
                speedMax = config.speed.endValue,
                accelXMin = config.accelX.startValue,
                accelXMax = config.accelX.endValue,
                accelYMin = config.accelY.startValue,
                accelYMax = config.accelY.endValue,
                angleMin = config.angle.startValue,
                angleMax = config.angle.endValue,
                rotationMin = config.rotation.startValue,
                rotationMax = config.rotation.endValue,
                rotationSpeedMin = config.rotationSpeed.startValue,
                rotationSpeedMax = config.rotationSpeed.endValue,
                duration = config.particleDuration,
                fadeOutDuration = config.particleFadeOutDuration,
                onParticleEnd = { particlesToRemove.add(it) }
            )
        }

}