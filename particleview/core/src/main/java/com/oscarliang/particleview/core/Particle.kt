package com.oscarliang.particleview.core

import android.graphics.Canvas
import android.graphics.Paint
import com.oscarliang.particleview.core.util.nextFloatSafely
import com.oscarliang.particleview.core.util.nextIntSafely
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Particle(
    val image: ParticleImage,
    startXMin: Float,
    startXMax: Float,
    startYMin: Float,
    startYMax: Float,
    speedMin: Float,
    speedMax: Float,
    accelXMin: Float,
    accelXMax: Float,
    accelYMin: Float,
    accelYMax: Float,
    angleMin: Int,
    angleMax: Int,
    rotationMin: Int,
    rotationMax: Int,
    rotationSpeedMin: Float,
    rotationSpeedMax: Float,
    density: Float
) {

    val width: Float = image.size * density
    val height: Float = image.size * image.ratio * density
    private val scale: Float = width / image.bitmap.width

    var positionX: Float = 0.0f
    var positionY: Float = 0.0f
    var speedX: Float = 0.0f
    var speedY: Float = 0.0f
    var accelX: Float = 0.0f
    var accelY: Float = 0.0f
    var rotatation: Float = 0.0f
    var rotationSpeed = 0.0f

    init {
        val speed = Random.nextFloatSafely(speedMin, speedMax)
        val angle = toRadians(Random.nextIntSafely(angleMin, angleMax).toDouble())
        speedX = speed * sin(angle).toFloat()
        speedY = speed * cos(angle).toFloat()
        accelX = Random.nextFloatSafely(accelXMin, accelXMax)
        accelY = Random.nextFloatSafely(accelYMin, accelYMax)
        positionX = Random.nextFloatSafely(startXMin, startXMax) - width / 2
        positionY = Random.nextFloatSafely(startYMin, startYMax) - height / 2
        rotatation = Random.nextIntSafely(rotationMin, rotationMax).toFloat()
        rotationSpeed = Random.nextFloatSafely(rotationSpeedMin, rotationSpeedMax)
    }

    fun update(elapsedMillis: Long) {
        speedX += accelX / 1000 * elapsedMillis
        speedY += accelY / 1000 * elapsedMillis
        positionX += speedX / 1000 * elapsedMillis
        positionY += speedY / 1000 * elapsedMillis
        rotatation += rotationSpeed / 1000 * elapsedMillis
    }

    fun draw(canvas: Canvas, paint: Paint) {
        canvas.save()
        canvas.translate(positionX, positionY)
        canvas.rotate(rotatation, width / 2, height / 2)
        canvas.scale(scale, scale, width / 2, height / 2)
        canvas.drawBitmap(image.bitmap, 0.0f, 0.0f, paint)
        canvas.restore()
    }

    fun isInBound(x: Float, y: Float): Boolean {
        // Check is (x, y) in particle bound
        return x >= positionX && x <= positionX + width
                && y >= positionY && y <= positionY + height
    }

}