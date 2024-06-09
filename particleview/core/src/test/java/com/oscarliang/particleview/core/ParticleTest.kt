package com.oscarliang.particleview.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ParticleTest {

    private val size = 100
    private val density = 1.0f

    private lateinit var bitmap: Bitmap
    private lateinit var particle: Particle

    @Before
    fun setUp() {
        bitmap = mockk<Bitmap>(relaxed = true).apply {
            every { width } returns size
            every { height } returns size
        }
        particle = Particle(
            image = ParticleImage(bitmap, size),
            startXMin = 0.0f,
            startXMax = 0.0f,
            startYMin = 0.0f,
            startYMax = 0.0f,
            speedMin = 100.0f,
            speedMax = 100.0f,
            accelXMin = 10.0f,
            accelXMax = 10.0f,
            accelYMin = 10.0f,
            accelYMax = 10.0f,
            angleMin = 0,
            angleMax = 0,
            rotationMin = 30,
            rotationMax = 30,
            rotationSpeedMin = 30.0f,
            rotationSpeedMax = 30.0f,
            density = density
        )
    }

    @Test
    fun testSize() {
        assertEquals(particle.width, size * density)
        assertEquals(particle.height, size * density)
    }

    @Test
    fun testAccel() {
        assertEquals(particle.accelX, 10.0f)
        assertEquals(particle.accelY, 10.0f)
    }

    @Test
    fun testSpeed() {
        // Angle = 0 and speed = 100 will produce (speedX = 0, speedY = 100)
        assertEquals(particle.speedX, 0.0f)
        assertEquals(particle.speedY, 100.0f)

        // Accel is 10 / 1000 * 100ms = 1 will produce (speedX = 1, speedY = 101)
        particle.update(100)
        assertEquals(particle.speedX, 1.0f)
        assertEquals(particle.speedY, 101.0f)

        // Accel is 10 / 1000 * 100ms = 1 will produce (speedX = 1, speedY = 101)
        particle.update(100)
        assertEquals(particle.speedX, 2.0f)
        assertEquals(particle.speedY, 102.0f)
    }

    @Test
    fun testPosition() {
        // Start position is (x = 0, y = 0)
        assertEquals(particle.positionX, 0.0f - particle.width / 2)
        assertEquals(particle.positionY, 0.0f - particle.height / 2)

        // Speed is (speedX = 1, speedY = 101)
        // X translation is 1 / 1000 / 100ms = 0.1
        // Y translation is 101 / 1000 / 100ms = 10.1
        // End position will produce (x = 0.1, y = 10.1)
        particle.update(100)
        assertEquals(particle.positionX, 0.1f - particle.width / 2)
        assertEquals(particle.positionY, 10.1f - particle.height / 2)

        // Speed is (speedX = 2, speedY = 102)
        // X translation is 2 / 1000 / 100ms = 0.2
        // Y translation is 102 / 1000 / 100ms = 10.2
        // End position will produce (x = 0.3, y = 20.3)
        particle.update(100)
        assertEquals(particle.positionX, 0.3f - particle.width / 2)
        assertEquals(particle.positionY, 20.3f - particle.height / 2)
    }

    @Test
    fun testRotation() {
        assertEquals(particle.rotatation, 30.0f)
        assertEquals(particle.rotationSpeed, 30.0f)

        // Rotation speed is 30 / 1000 * 100ms = 3
        particle.update(100)
        assertEquals(particle.rotatation, 33.0f)
        particle.update(100)
        assertEquals(particle.rotatation, 36.0f)
    }

    @Test
    fun testDraw() {
        val canvas = mockk<Canvas>(relaxed = true)
        val paint = mockk<Paint>(relaxed = true)
        particle.update(100)
        particle.draw(canvas, paint)

        verify { canvas.save() }
        verify { canvas.translate(0.1f - particle.width / 2, 10.1f - particle.height / 2) }
        verify { canvas.rotate(33.0f, particle.width / 2, particle.height / 2) }
        verify { canvas.scale(density, density, particle.width / 2, particle.height / 2) }
        verify { canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint) }
        verify { canvas.restore() }
    }

    @Test
    fun testIsInBound() {
        particle.update(100)

        // Bound at (Left = -49.9, Top = -39.9, Right = 50.1, Bottom = 60.1)
        assertTrue(particle.isInBound(0.0f, 0.0f))
        assertFalse(particle.isInBound(60.0f, 60.0f))
    }

}