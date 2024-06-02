package com.oscarliang.particleview.core

import android.graphics.Bitmap
import android.graphics.Paint
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.IntOffset
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ParticleSystemTest {

    private val paint = mockk<Paint>(relaxed = true)
    private lateinit var particleSystem: ParticleSystem

    @Before
    fun setUp() {
        val bitmap = mockk<Bitmap>(relaxed = true)
        particleSystem = ParticleSystem(
            images = listOf(ParticleImage(bitmap, 100)),
            startX = FloatOffset(0.0f),
            startY = FloatOffset(0.0f),
            speed = FloatOffset(100.0f),
            accelX = FloatOffset(10.0f),
            accelY = FloatOffset(10.0f),
            angle = IntOffset(0),
            rotation = IntOffset(0),
            rotationSpeed = FloatOffset(30.0f),
            countPerSecond = 10,
            duration = 1000,
            fadeOutDuration = 500,
            fadeOutEnable = true,
            density = 1.0f,
            paint = paint
        )
    }

    @After
    fun tearDown() {
        particleSystem.release()
    }

    @Test
    fun testAddParticle() {
        particleSystem.update(0, 0)
        assertEquals(particleSystem.particles.size, 0)

        particleSystem.update(100, 0)
        assertEquals(particleSystem.particles.size, 1)

        particleSystem.update(200, 0)
        assertEquals(particleSystem.particles.size, 2)
    }

    @Test
    fun testFadeOut() {
        particleSystem.update(1000, 0)
        verify { paint.alpha = 255 }

        particleSystem.update(1250, 0)
        verify { paint.alpha = 127 }

        particleSystem.update(1500, 0)
        verify { paint.alpha = 0 }
    }

}