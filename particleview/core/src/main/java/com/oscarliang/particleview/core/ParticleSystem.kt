package com.oscarliang.particleview.core

import android.graphics.Canvas
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.IntOffset
import kotlin.random.Random

class ParticleSystem(
    val images: List<ParticleImage>,
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
) {

    var width: Int = 0
    var height: Int = 0

    private val particles = mutableListOf<Particle>()
    private val particlesPool = mutableListOf<Particle>()
    private val particlesToRemove = mutableListOf<Particle>()

    private var current = 0L

    fun update(currentMillis: Long, elapsedMillis: Long) {
        val durationPerParticle = particleDuration + particleFadeOutDuration
        if (currentMillis < duration - durationPerParticle) {
            val millisPerParticle = 1000 / particlePerSecond
            current += elapsedMillis
            if (current >= millisPerParticle) {
                particles.add(getOneParticle())
                current = 0
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

    fun draw(canvas: Canvas) {
        particles.forEach { particle ->
            particle.draw(canvas)
        }
    }

    fun release() {
        particles.clear()
        particlesPool.clear()
        particlesToRemove.clear()
    }

    fun getParticleAt(x: Float, y: Float): Particle? {
        particles.forEach { particle ->
            if (particle.isInBound(x, y)) {
                return particle
            }
        }
        return null
    }

    private fun getOneParticle() =
        if (particlesPool.isNotEmpty()) {
            val particle = particlesPool.removeAt(0)
            particle.reset()
            particle
        } else {
            Particle(
                image = images[Random.nextInt(images.size)],
                startXMin = startX.startValue * width,
                startXMax = startX.endValue * width,
                startYMin = startY.startValue * height,
                startYMax = startY.endValue * height,
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