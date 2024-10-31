package com.oscarliang.xml

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.oscarliang.particleview.core.ParticleConfig
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset
import com.oscarliang.particleview.xml.ParticleView
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var particleView: ParticleView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        particleView = findViewById(R.id.particle_view)

        findViewById<Button>(R.id.btn_snow).setOnClickListener {
            lifecycleScope.launch { playSnow() }
            onAnimationStart()
        }
        findViewById<Button>(R.id.btn_explosion).setOnClickListener {
            lifecycleScope.launch { playExplosion() }
            onAnimationStart()
        }
        findViewById<Button>(R.id.btn_confetti).setOnClickListener {
            lifecycleScope.launch { playConfetti() }
            onAnimationStart()
        }
        findViewById<Button>(R.id.btn_poker).setOnClickListener {
            lifecycleScope.launch { playPoker() }
            onAnimationStart()
        }
        findViewById<Button>(R.id.btn_rain).setOnClickListener {
            lifecycleScope.launch { playRain() }
            onAnimationStart()
        }
        findViewById<Button>(R.id.btn_bubble).setOnClickListener {
            lifecycleScope.launch { playBubble() }
            onAnimationStart()
        }
    }

    override fun onStart() {
        super.onStart()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onPause() {
        super.onPause()
        particleView.pause()
    }

    override fun onResume() {
        super.onResume()
        particleView.resume()
    }

    private fun onAnimationEnd() {
        findViewById<View>(R.id.layout_button).isVisible = true
    }

    private fun onAnimationStart() {
        findViewById<View>(R.id.layout_button).isVisible = false
    }

    private suspend fun playSnow() {
        particleView.start(
            configs = listOf(
                ParticleConfig(
                    images = listOf(
                        Image(
                            imageId = R.drawable.snowflake,
                            size = 40
                        ),
                        Image(
                            imageId = R.drawable.snowflake,
                            size = 30
                        )
                    ),
                    startX = FloatOffset(0.0f, 1.0f),
                    startY = FloatOffset(-0.1f),
                    angle = IntOffset(0),
                    speed = FloatOffset(300.0f, 600.0f),
                    accelY = FloatOffset(300.0f, 600.0f),
                    particlePerSecond = 25
                )
            ),
            duration = 6000,
            onParticlesEnd = { onAnimationEnd() }
        )
    }

    private suspend fun playExplosion() {
        particleView.start(
            configs = listOf(
                ParticleConfig(
                    images = listOf(
                        Image(
                            imageId = R.drawable.particle_blue,
                            size = 10,
                        ),
                        Image(
                            imageId = R.drawable.particle_green,
                            size = 10,
                        ),
                        Image(
                            imageId = R.drawable.particle_red,
                            size = 12,
                        ),
                        Image(
                            imageId = R.drawable.particle_white,
                            size = 12,
                        ),
                        Image(
                            imageId = R.drawable.particle_yellow,
                            size = 12,
                        ),
                        Image(
                            imageId = R.drawable.particle_blue,
                            size = 15,
                        ),
                        Image(
                            imageId = R.drawable.particle_green,
                            size = 15,
                        ),
                        Image(
                            imageId = R.drawable.particle_red,
                            size = 18,
                        ),
                        Image(
                            imageId = R.drawable.particle_white,
                            size = 18,
                        ),
                        Image(
                            imageId = R.drawable.particle_yellow,
                            size = 18,
                        )
                    ),
                    startX = FloatOffset(0.5f),
                    startY = FloatOffset(0.3f),
                    speed = FloatOffset(500.0f, 1000.0f),
                    accelY = FloatOffset(1500.0f, 2000.0f),
                    angle = IntOffset(160, 200),
                    particleDuration = 1200,
                    particlePerSecond = 50,
                )
            ),
            duration = 5000,
            onParticlesEnd = { onAnimationEnd() }
        )
    }

    private suspend fun playConfetti() {
        particleView.start(
            configs = listOf(
                ParticleConfig(
                    images = listOf(
                        Image(
                            imageId = R.drawable.confetti_blue,
                            size = 20,
                        ),
                        Image(
                            imageId = R.drawable.confetti_pink,
                            size = 20,
                        )
                    ),
                    startX = FloatOffset(0.0f),
                    startY = FloatOffset(0.5f),
                    speed = FloatOffset(1200.0f, 1500.0f),
                    accelX = FloatOffset(-500.0f, -300.0f),
                    accelY = FloatOffset(1000.0f, 1200.0f),
                    angle = IntOffset(140, 160),
                    rotationSpeed = FloatOffset(-30.0f, 30.0f),
                    particleDuration = 2300,
                    particleFadeOutDuration = 300,
                    particlePerSecond = 20
                ),
                ParticleConfig(
                    images = listOf(
                        Image(
                            imageId = R.drawable.confetti_green,
                            size = 20,
                        ),
                        Image(
                            imageId = R.drawable.confetti_yellow,
                            size = 20,
                        )
                    ),
                    startX = FloatOffset(1.0f),
                    startY = FloatOffset(0.5f),
                    speed = FloatOffset(-1500.0f, -1200.0f),
                    accelX = FloatOffset(300.0f, 500.0f),
                    accelY = FloatOffset(1000.0f, 1200.0f),
                    angle = IntOffset(20, 40),
                    rotationSpeed = FloatOffset(-30.0f, 30.0f),
                    particleDuration = 2300,
                    particleFadeOutDuration = 300,
                    particlePerSecond = 20
                )
            ),
            duration = 6000,
            onParticlesEnd = { onAnimationEnd() }
        )
    }

    private suspend fun playPoker() {
        particleView.start(
            configs = listOf(
                ParticleConfig(
                    images = listOf(
                        Image(
                            imageId = R.drawable.poker_spade,
                            size = 80,
                        ),
                        Image(
                            imageId = R.drawable.poker_heart,
                            size = 80,
                        ),
                        Image(
                            imageId = R.drawable.poker_diamond,
                            size = 80,
                        ),
                        Image(
                            imageId = R.drawable.poker_club,
                            size = 80,
                        )
                    ),
                    startX = FloatOffset(0.5f),
                    startY = FloatOffset(0.5f),
                    speed = FloatOffset(-1000.0f, 1000.0f),
                    accelX = FloatOffset(-500.0f, 500.0f),
                    accelY = FloatOffset(200.0f, 300.0f),
                    rotationSpeed = FloatOffset(-60.0f, 60.0f),
                    particleDuration = 1000,
                    particlePerSecond = 10
                )
            ),
            duration = 5000,
            onParticlesEnd = { onAnimationEnd() }
        )
    }

    private suspend fun playRain() {
        particleView.start(
            configs = listOf(
                ParticleConfig(
                    images = listOf(
                        Image(
                            imageId = R.drawable.raindrop,
                            size = 8
                        ),
                        Image(
                            imageId = R.drawable.raindrop,
                            size = 10
                        ),
                        Image(
                            imageId = R.drawable.raindrop,
                            size = 12
                        )
                    ),
                    startX = FloatOffset(0.0f, 2.5f),
                    startY = FloatOffset(-0.5f),
                    angle = IntOffset(-30),
                    speed = FloatOffset(3000.0f, 4000.0f),
                    rotation = IntOffset(210),
                    particleDuration = 600,
                    particleFadeOutDuration = 300,
                    particlePerSecond = 60
                )
            ),
            duration = 5000,
            onParticlesEnd = { onAnimationEnd() }
        )
    }

    private suspend fun playBubble() {
        particleView.start(
            configs = listOf(
                ParticleConfig(
                    images = listOf(
                        Image(
                            imageId = R.drawable.bubble,
                            size = 60
                        ),
                        Image(
                            imageId = R.drawable.bubble,
                            size = 80
                        )
                    ),
                    startX = FloatOffset(0.0f, 1.0f),
                    startY = FloatOffset(1.0f, 1.5f),
                    angle = IntOffset(180),
                    speed = FloatOffset(300.0f, 500.0f),
                    accelY = FloatOffset(-1000.0f, -500.0f),
                    rotationSpeed = FloatOffset(30.0f, 60.0f),
                    particleDuration = 1600,
                    particlePerSecond = 20
                )
            ),
            duration = 8000,
            onParticlesEnd = { onAnimationEnd() }
        )
    }

}