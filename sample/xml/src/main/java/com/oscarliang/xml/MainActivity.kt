package com.oscarliang.xml

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset
import com.oscarliang.particleview.xml.ParticleView

class MainActivity : AppCompatActivity() {

    private lateinit var particleView: ParticleView
    private lateinit var btnSnow: Button
    private lateinit var btnExplosion: Button
    private lateinit var btnConfetti: Button
    private lateinit var btnPoker: Button
    private lateinit var btnRain: Button
    private lateinit var btnBubble: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        particleView = findViewById(R.id.particle_view)
        btnSnow = findViewById(R.id.btn_snow)
        btnExplosion = findViewById(R.id.btn_explosion)
        btnConfetti = findViewById(R.id.btn_confetti)
        btnPoker = findViewById(R.id.btn_poker)
        btnRain = findViewById(R.id.btn_rain)
        btnBubble = findViewById(R.id.btn_bubble)

        btnSnow.setOnClickListener {
            playSnow()
            hideButton()
        }
        btnExplosion.setOnClickListener {
            playExplosion()
            hideButton()
        }
        btnConfetti.setOnClickListener {
            playConfetti()
            hideButton()
        }
        btnPoker.setOnClickListener {
            playPoker()
            hideButton()
        }
        btnRain.setOnClickListener {
            playRain()
            hideButton()
        }
        btnBubble.setOnClickListener {
            playBubble()
            hideButton()
        }
    }

    private fun showButton() {
        btnSnow.isVisible = true
        btnExplosion.isVisible = true
        btnConfetti.isVisible = true
        btnPoker.isVisible = true
        btnRain.isVisible = true
        btnBubble.isVisible = true
    }

    private fun hideButton() {
        btnSnow.isVisible = false
        btnExplosion.isVisible = false
        btnConfetti.isVisible = false
        btnPoker.isVisible = false
        btnRain.isVisible = false
        btnBubble.isVisible = false
    }

    private fun playSnow() {
        particleView.start(
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
            countPerSecond = 25,
            duration = 5000,
            onAnimationEndListener = { showButton() }
        )
    }

    private fun playExplosion() {
        particleView.start(
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
            countPerSecond = 50,
            duration = 5000,
            onAnimationEndListener = { showButton() }
        )
    }

    private fun playConfetti() {
        particleView.start(
            images = listOf(
                Image(
                    imageId = R.drawable.confetti_blue,
                    size = 20,
                ),
                Image(
                    imageId = R.drawable.confetti_pink,
                    size = 20,
                ),
                Image(
                    imageId = R.drawable.confetti_green,
                    size = 20,
                ),
                Image(
                    imageId = R.drawable.confetti_yellow,
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
            countPerSecond = 20,
            duration = 5000,
            onAnimationEndListener = { showButton() }
        )
    }

    private fun playPoker() {
        particleView.start(
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
            countPerSecond = 10,
            duration = 5000,
            onAnimationEndListener = { showButton() }
        )
    }

    private fun playRain() {
        particleView.start(
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
            countPerSecond = 60,
            duration = 5000,
            onAnimationEndListener = { showButton() }
        )
    }

    private fun playBubble() {
        particleView.start(
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
            countPerSecond = 20,
            duration = 5000,
            onAnimationEndListener = { showButton() }
        )
    }

}