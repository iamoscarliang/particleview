package com.oscarliang.particleview.core

import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ParticleTest {

    private val size = 100
    private val density = 1.0f

    private lateinit var particle: Particle

    @Before
    fun setUp() {
        particle = Particle(
            image = Image(0, size),
            startX = FloatOffset(0.0f),
            startY = FloatOffset(0.0f),
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
            duration = 500,
            fadeOutDuration = 200,
            density = density,
            onParticleEnd = {}
        ).apply { reset(IntOffset(0)) }
    }

    @Test
    fun testSize() {
        assertEquals(particle.width, size * density)
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
        assertEquals(particle.positionX, 0.0f)
        assertEquals(particle.positionY, 0.0f)

        // Speed is (speedX = 1, speedY = 101)
        // X translation is 1 / 1000 / 100ms = 0.1
        // Y translation is 101 / 1000 / 100ms = 10.1
        // End position will produce (x = 0.1, y = 10.1)
        particle.update(100)
        assertEquals(particle.positionX, 0.1f)
        assertEquals(particle.positionY, 10.1f)

        // Speed is (speedX = 2, speedY = 102)
        // X translation is 2 / 1000 / 100ms = 0.2
        // Y translation is 102 / 1000 / 100ms = 10.2
        // End position will produce (x = 0.3, y = 20.3)
        particle.update(100)
        assertEquals(particle.positionX, 0.3f)
        assertEquals(particle.positionY, 20.3f)
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
    fun testFadeOut() {
        // Fast forward to start of fadeout, alpha = 255
        particle.update(500)
        assertEquals(particle.alpha, 255)

        // Alpha is 255 * (1 - 0.5) = 127
        particle.update(100)
        assertEquals(particle.alpha, 127)

        // Alpha is 255 * (1 - 1) = 0
        particle.update(100)
        assertEquals(particle.alpha, 0)
    }

}