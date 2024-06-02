package com.oscarliang.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oscarliang.compose.ui.theme.ParticleViewTheme
import com.oscarliang.particleview.compose.ParticleView
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParticleViewTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ParticleScreen()
                }
            }
        }
    }
}

@Composable
fun ParticleScreen() {

    var type by remember { mutableStateOf(ParticleType.NONE) }

    Column {
        Box(modifier = Modifier.weight(1.0f)) {
            when (type) {
                ParticleType.ENVELOP -> EnvelopView { type = ParticleType.NONE }
                ParticleType.CONFETTI -> ConfettiView { type = ParticleType.NONE }
                ParticleType.POKER -> PokerView { type = ParticleType.NONE }
                else -> {}
            }
        }
        if (type == ParticleType.NONE) {
            ButtonSection(
                onEnvelopClick = { type = ParticleType.ENVELOP },
                onConfettiClick = { type = ParticleType.CONFETTI },
                onPokerClick = { type = ParticleType.POKER }
            )
        }
    }
}

@Composable
fun EnvelopView(
    onAnimationEnd: () -> Unit
) {

    var isRunning by remember { mutableStateOf(true) }

    ParticleView(
        modifier = Modifier.fillMaxSize(),
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
        isRunning = isRunning,
        onParticleClickListener = { particle ->
            if (particle.image.tag == "envelop") {
                isRunning = false
            }
        },
        onAnimationEndListener = {
            onAnimationEnd()
        }
    )
}

@Composable
fun ConfettiView(
    onAnimationEnd: () -> Unit
) {
    ParticleView(
        modifier = Modifier.fillMaxSize(),
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
            onAnimationEnd()
        }
    )
}

@Composable
fun PokerView(
    onAnimationEnd: () -> Unit
) {
    ParticleView(
        modifier = Modifier.fillMaxSize(),
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
            onAnimationEnd()
        }
    )
}

@Composable
fun ButtonSection(
    onEnvelopClick: () -> Unit,
    onConfettiClick: () -> Unit,
    onPokerClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        Button(
            onClick = { onEnvelopClick() },
            modifier = Modifier.weight(1.0f)
        ) {
            Text(text = "Envelop")
        }
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            onClick = { onConfettiClick() },
            modifier = Modifier.weight(1.0f)
        ) {
            Text(text = "Confetti")
        }
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            onClick = { onPokerClick() },
            modifier = Modifier.weight(1.0f)
        ) {
            Text(text = "Poker")
        }
    }
}

@Preview
@Composable
private fun ParticleScreenPreview() {
    ParticleViewTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ParticleScreen()
        }
    }
}

enum class ParticleType {
    ENVELOP,
    CONFETTI,
    POKER,
    NONE
}