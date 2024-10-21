package com.oscarliang.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.oscarliang.compose.ui.theme.ParticleViewTheme
import com.oscarliang.particleview.compose.ParticleView
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParticleViewTheme {
                ParticleScreen()
            }
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

}

@Composable
fun ParticleScreen() {

    var type by rememberSaveable { mutableStateOf(ParticleType.NONE) }
    var isPause by rememberSaveable { mutableStateOf(false) }

    // Pause and resume when lifecycle change
    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        isPause = true
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        isPause = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
        ) {
            when (type) {
                ParticleType.SNOW -> SnowView(isPause) { type = ParticleType.NONE }
                ParticleType.EXPLOSION -> ExplosionView(isPause) { type = ParticleType.NONE }
                ParticleType.CONFETTI -> ConfettiView(isPause) { type = ParticleType.NONE }
                ParticleType.POKER -> PokerView(isPause) { type = ParticleType.NONE }
                ParticleType.RAIN -> RainView(isPause) { type = ParticleType.NONE }
                ParticleType.BUBBLE -> BubbleView(isPause) { type = ParticleType.NONE }
                else -> Unit
            }
        }

        if (type == ParticleType.NONE) {
            AnimationButtons(
                onSnowClick = { type = ParticleType.SNOW },
                onExplosionClick = { type = ParticleType.EXPLOSION },
                onConfettiClick = { type = ParticleType.CONFETTI },
                onPokerClick = { type = ParticleType.POKER },
                onRainClick = { type = ParticleType.RAIN },
                onBubbleClick = { type = ParticleType.BUBBLE }
            )
        }
    }
}

@Composable
fun SnowView(
    isPause: Boolean,
    onParticlesEnd: () -> Unit
) {
    ParticleView(
        modifier = Modifier.fillMaxSize(),
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
        particlePerSecond = 25,
        duration = 6000,
        isPause = isPause,
        onParticlesEnd = { onParticlesEnd() }
    )
}

@Composable
fun BoxScope.ExplosionView(
    isPause: Boolean,
    onParticlesEnd: () -> Unit
) {
    ParticleView(
        modifier = Modifier
            .aspectRatio(1.0f)
            .align(Alignment.Center),
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
        duration = 5000,
        isPause = isPause,
        onParticlesEnd = { onParticlesEnd() }
    )
}

@Composable
fun BoxScope.ConfettiView(
    isPause: Boolean,
    onParticlesEnd: () -> Unit
) {
    ParticleView(
        modifier = Modifier
            .aspectRatio(1.0f)
            .align(Alignment.CenterStart),
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
        particleDuration = 2300,
        particleFadeOutDuration = 300,
        particlePerSecond = 20,
        duration = 6000,
        isPause = isPause,
        onParticlesEnd = { onParticlesEnd() }
    )
}

@Composable
fun BoxScope.PokerView(
    isPause: Boolean,
    onParticlesEnd: () -> Unit
) {
    ParticleView(
        modifier = Modifier
            .aspectRatio(1.0f)
            .align(Alignment.Center),
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
        particlePerSecond = 10,
        duration = 5000,
        isPause = isPause,
        onParticlesEnd = { onParticlesEnd() }
    )
}

@Composable
fun RainView(
    isPause: Boolean,
    onParticlesEnd: () -> Unit
) {
    ParticleView(
        modifier = Modifier.fillMaxSize(),
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
        particlePerSecond = 60,
        duration = 5000,
        isPause = isPause,
        onParticlesEnd = { onParticlesEnd() }
    )
}

@Composable
fun BubbleView(
    isPause: Boolean,
    onParticlesEnd: () -> Unit
) {
    ParticleView(
        modifier = Modifier.fillMaxSize(),
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
        particlePerSecond = 20,
        duration = 8000,
        isPause = isPause,
        onParticlesEnd = { onParticlesEnd() }
    )
}

@Composable
fun AnimationButtons(
    onSnowClick: () -> Unit,
    onExplosionClick: () -> Unit,
    onConfettiClick: () -> Unit,
    onPokerClick: () -> Unit,
    onRainClick: () -> Unit,
    onBubbleClick: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(
                onClick = { onSnowClick() },
                modifier = Modifier.weight(1.0f)
            ) {
                Text(text = stringResource(id = R.string.snow))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { onExplosionClick() },
                modifier = Modifier.weight(1.0f)
            ) {
                Text(text = stringResource(id = R.string.explosion))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { onConfettiClick() },
                modifier = Modifier.weight(1.0f)
            ) {
                Text(text = stringResource(id = R.string.confetti))
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(
                onClick = { onPokerClick() },
                modifier = Modifier.weight(1.0f)
            ) {
                Text(text = stringResource(id = R.string.poker))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { onRainClick() },
                modifier = Modifier.weight(1.0f)
            ) {
                Text(text = stringResource(id = R.string.rain))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { onBubbleClick() },
                modifier = Modifier.weight(1.0f)
            ) {
                Text(text = stringResource(id = R.string.bubble))
            }
        }
    }
}

@Preview
@Composable
private fun ParticleScreenPreview() {
    ParticleViewTheme {
        ParticleScreen()
    }
}

enum class ParticleType {
    SNOW,
    EXPLOSION,
    CONFETTI,
    POKER,
    RAIN,
    BUBBLE,
    NONE
}