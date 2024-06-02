package com.oscarliang.particleview.core

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.VisibleForTesting
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.IntOffset
import kotlin.random.Random

class ParticleSystem(
    val images: List<ParticleImage>,
    var startX: FloatOffset,
    var startY: FloatOffset,
    var speed: FloatOffset,
    var accelX: FloatOffset,
    var accelY: FloatOffset,
    var angle: IntOffset,
    var rotation: IntOffset,
    var rotationSpeed: FloatOffset,
    var countPerSecond: Int,
    var duration: Long,
    var fadeOutDuration: Long,
    var fadeOutEnable: Boolean,
    var density: Float,
    val paint: Paint = Paint()
) {

    var width: Int = 0
    var height: Int = 0

    @VisibleForTesting
    val particles = ArrayList<Particle>()

    fun update(currentMillis: Long, elapsedMillis: Long) {
        val millisPerCount = 1000 / countPerSecond
        if (currentMillis >= (particles.size + 1) * millisPerCount) {
            addOneParticle()
        }

        particles.forEach { particle ->
            particle.update(elapsedMillis)
        }
        if (fadeOutEnable && currentMillis >= duration) {
            // Update particle's alpha base on fade duration percentage
            val fadePercent = (currentMillis - duration) * 1.0f / fadeOutDuration
            paint.alpha = (255 * (1 - fadePercent)).toInt()
        }
    }

    fun draw(canvas: Canvas) {
        particles.forEach { particle ->
            particle.draw(canvas, paint)
        }
    }

    fun release() {
        particles.clear()
    }

    fun getParticleAt(x: Float, y: Float): Particle? {
        particles.forEach { particle ->
            if (particle.isInBound(x, y)) {
                return particle
            }
        }
        return null
    }

    private fun addOneParticle() {
        particles.add(
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
                density = density
            )
        )
    }

}