package com.oscarliang.xml

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset
import com.oscarliang.particleview.xml.ParticleView

class MainActivity : AppCompatActivity() {

    private lateinit var particleView: ParticleView
    private lateinit var btnEnvelop: Button
    private lateinit var btnConfetti: Button
    private lateinit var btnPoker: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        particleView = findViewById(R.id.particle_view)
        btnEnvelop = findViewById(R.id.btn_envelop)
        btnConfetti = findViewById(R.id.btn_confetti)
        btnPoker = findViewById(R.id.btn_poker)

        btnEnvelop.setOnClickListener {
            playEnvelop()
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
    }

    private fun showButton() {
        btnEnvelop.isVisible = true
        btnConfetti.isVisible = true
        btnPoker.isVisible = true
    }

    private fun hideButton() {
        btnEnvelop.isVisible = false
        btnConfetti.isVisible = false
        btnPoker.isVisible = false
    }

    private fun playEnvelop() {
        particleView.start(
            images = listOf(
                Image(
                    imageId = R.drawable.envelop,
                    size = 150,
                    tag = "envelop"
                ),
                Image(
                    imageId = R.drawable.coin,
                    size = 100,
                    tag = "coin"
                )
            ),
            startX = FloatOffset(0.0f, 1.0f),
            startY = FloatOffset(-0.2f),
            speed = FloatOffset(1200.0f, 1500.0f),
            accelX = FloatOffset(-300.0f, 300.0f),
            accelY = FloatOffset(500.0f, 800.0f),
            angle = IntOffset(-30, 30),
            rotationSpeed = FloatOffset(-60.0f, 60.0f),
            countPerSecond = 10,
            duration = 5000,
            onParticleClickListener = { particle ->
                if (particle.image.tag == "envelop") {
                    particleView.cancel()
                }
            },
            onAnimationEndListener = {
                showButton()
            }
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
            onAnimationEndListener = {
                showButton()
            }
        )
    }

    private fun playPoker() {
        particleView.start(
            images = listOf(
                Image(
                    imageId = R.drawable.poker_spade,
                    size = 100,
                ),
                Image(
                    imageId = R.drawable.poker_heart,
                    size = 100,
                ),
                Image(
                    imageId = R.drawable.poker_diamond,
                    size = 100,
                ),
                Image(
                    imageId = R.drawable.poker_club,
                    size = 100,
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
            onAnimationEndListener = {
                showButton()
            }
        )
    }

}