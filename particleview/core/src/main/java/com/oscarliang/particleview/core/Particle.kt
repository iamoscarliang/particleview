package com.oscarliang.particleview.core

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcelable
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset
import com.oscarliang.particleview.core.util.nextFloatSafely
import com.oscarliang.particleview.core.util.nextIntSafely
import kotlinx.parcelize.Parcelize
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Parcelize
data class Particle(
    val image: Image,
    val startX: FloatOffset,
    val startY: FloatOffset,
    val speedMin: Float,
    val speedMax: Float,
    val accelXMin: Float,
    val accelXMax: Float,
    val accelYMin: Float,
    val accelYMax: Float,
    val angleMin: Int,
    val angleMax: Int,
    val rotationMin: Int,
    val rotationMax: Int,
    val rotationSpeedMin: Float,
    val rotationSpeedMax: Float,
    val duration: Long,
    val fadeOutDuration: Long,
    val density: Float,
    val onParticleEnd: (Particle) -> Unit
) : Parcelable {

    val width = image.size * density

    var positionX = 0.0f
    var positionY = 0.0f
    var speedX = 0.0f
    var speedY = 0.0f
    var accelX = 0.0f
    var accelY = 0.0f
    var rotatation = 0.0f
    var rotationSpeed = 0.0f
    var alpha = 255

    private var currentTime = 0L

    fun reset(drawArea: IntOffset) {
        val speed = Random.nextFloatSafely(speedMin, speedMax)
        val angle = toRadians(Random.nextIntSafely(angleMin, angleMax).toDouble())
        speedX = speed * sin(angle).toFloat()
        speedY = speed * cos(angle).toFloat()
        accelX = Random.nextFloatSafely(accelXMin, accelXMax)
        accelY = Random.nextFloatSafely(accelYMin, accelYMax)
        positionX = Random.nextFloatSafely(
            startX.startValue * drawArea.startValue,
            startX.endValue * drawArea.startValue
        )
        positionY = Random.nextFloatSafely(
            startY.startValue * drawArea.endValue,
            startY.endValue * drawArea.endValue
        )
        rotatation = Random.nextIntSafely(rotationMin, rotationMax).toFloat()
        rotationSpeed = Random.nextFloatSafely(rotationSpeedMin, rotationSpeedMax)
        alpha = 255
        currentTime = 0L
    }

    fun update(elapsedMillis: Long) {
        speedX += accelX / 1000 * elapsedMillis
        speedY += accelY / 1000 * elapsedMillis
        positionX += speedX / 1000 * elapsedMillis
        positionY += speedY / 1000 * elapsedMillis
        rotatation += rotationSpeed / 1000 * elapsedMillis
        currentTime += elapsedMillis

        // Update alpha base on fade percentage
        if (currentTime >= duration) {
            val fadePercent = (currentTime - duration) * 1.0f / fadeOutDuration
            alpha = (255 * (1 - fadePercent)).toInt()
        }

        // Remove and recycle the particle when reach total duration
        if (currentTime >= duration + fadeOutDuration) {
            onParticleEnd(this)
            currentTime = 0L
        }
    }

    fun draw(bitmapPool: BitmapPool, canvas: Canvas, paint: Paint) {
        val bitmap = bitmapPool.get(image.imageId) ?: return
        val height = width * (bitmap.height / bitmap.width)
        val scale = width / bitmap.width
        canvas.save()
        canvas.translate(positionX - width / 2, positionY - height / 2)
        canvas.rotate(rotatation, width / 2, height / 2)
        canvas.scale(scale, scale, width / 2, height / 2)
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint.apply { alpha = this@Particle.alpha })
        canvas.restore()
    }

}