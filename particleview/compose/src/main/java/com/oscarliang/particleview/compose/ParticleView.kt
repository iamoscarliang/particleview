package com.oscarliang.particleview.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import com.oscarliang.particleview.core.DEFAULT_ACCEL_X
import com.oscarliang.particleview.core.DEFAULT_ACCEL_Y
import com.oscarliang.particleview.core.DEFAULT_ANGLE
import com.oscarliang.particleview.core.DEFAULT_DURATION
import com.oscarliang.particleview.core.DEFAULT_PARTICLE_DURATION
import com.oscarliang.particleview.core.DEFAULT_PARTICLE_FADE_OUT_DURATION
import com.oscarliang.particleview.core.DEFAULT_PARTICLE_PER_SECOND
import com.oscarliang.particleview.core.DEFAULT_ROTATION
import com.oscarliang.particleview.core.DEFAULT_ROTATION_SPEED
import com.oscarliang.particleview.core.DEFAULT_SPEED
import com.oscarliang.particleview.core.DEFAULT_START_X
import com.oscarliang.particleview.core.DEFAULT_START_Y
import com.oscarliang.particleview.core.Particle
import com.oscarliang.particleview.core.ParticleImage
import com.oscarliang.particleview.core.ParticleSystem
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset
import com.oscarliang.particleview.core.util.toBitmap
import kotlinx.coroutines.isActive

/**
 *  Parent view to hold the particle and control the playback of the animation
 *
 * @param modifier - the Modifier to be applied
 * @param images - the image of the particle
 * @param startX - the start x position of the particle. Range from [0.0f, 1.0f] relative to the view's width
 * @param startY - the start y position of the particle. Range from [0.0f, 1.0f] relative to the view's height
 * @param speed - the speed of the particle moving in (px / per second)
 * @param accelX - the x acceleration added to the speed per second
 * @param accelY - the y acceleration added to the speed per second
 * @param angle - the direction of the particle's speed. Range from [0, 360].
 * Top - 180, Right - 90, Bottom - 0, Left - 270
 * @param rotation - the start rotation of the particle. Range from [0, 360]
 * @param rotationSpeed - the rotation speed of the particle rotating in (degree / per second)
 * @param particleDuration - the duration of the individual particle
 * @param particleFadeOutDuration - the duration of the fade out effect of particle
 * @param particlePerSecond - the amount of particle being emitted per second
 * @param duration - the duration of the animation
 * @param isRunning - controls the current state of the animation
 * @param onParticleClickListener - callback being executed when any particle is clicked
 * @param onAnimationEndListener - callback being executed when end of the animation
 */
@Composable
fun ParticleView(
    modifier: Modifier = Modifier,
    images: List<Image>,
    startX: FloatOffset = DEFAULT_START_X,
    startY: FloatOffset = DEFAULT_START_Y,
    speed: FloatOffset = DEFAULT_SPEED,
    accelX: FloatOffset = DEFAULT_ACCEL_X,
    accelY: FloatOffset = DEFAULT_ACCEL_Y,
    angle: IntOffset = DEFAULT_ANGLE,
    rotation: IntOffset = DEFAULT_ROTATION,
    rotationSpeed: FloatOffset = DEFAULT_ROTATION_SPEED,
    particleDuration: Long = DEFAULT_PARTICLE_DURATION,
    particleFadeOutDuration: Long = DEFAULT_PARTICLE_FADE_OUT_DURATION,
    particlePerSecond: Int = DEFAULT_PARTICLE_PER_SECOND,
    duration: Long = DEFAULT_DURATION,
    isRunning: Boolean = true,
    onParticleClickListener: (Particle) -> Unit = {},
    onAnimationEndListener: () -> Unit = {},
) {

    val resources = LocalContext.current.resources
    val density = LocalDensity.current.density

    val particleSystem by remember {
        mutableStateOf(
            ParticleSystem(
                images = images.map {
                    ParticleImage(
                        bitmap = resources.getDrawable(it.imageId, null).toBitmap(),
                        size = it.size,
                        tag = it.tag
                    )
                },
                startX = startX,
                startY = startY,
                angle = angle,
                speed = speed,
                accelX = accelX,
                accelY = accelY,
                rotation = rotation,
                rotationSpeed = rotationSpeed,
                particleDuration = particleDuration,
                particleFadeOutDuration = particleFadeOutDuration,
                particlePerSecond = particlePerSecond,
                duration = duration,
                density = density
            )
        )
    }

    var lastFrameMillis by remember { mutableLongStateOf(0L) }
    var currentFrameMillis by remember { mutableLongStateOf(0L) }

    if (!isRunning) {
        currentFrameMillis = duration
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            if (currentFrameMillis >= duration) {
                particleSystem.release()
                onAnimationEndListener()
                break
            }
            withFrameMillis { frameMillis ->
                val elapsedMillis = if (lastFrameMillis > 0) (frameMillis - lastFrameMillis) else 0
                lastFrameMillis = frameMillis
                currentFrameMillis += elapsedMillis
                particleSystem.update(currentFrameMillis, elapsedMillis)
            }
        }
    }

    Canvas(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                particleSystem.width = layoutCoordinates.size.width
                particleSystem.height = layoutCoordinates.size.height
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { position ->
                        val selected = particleSystem.getParticleAt(position.x, position.y)
                        if (selected != null) {
                            onParticleClickListener(selected)
                        }
                    }
                )
            },
        onDraw = {
            currentFrameMillis.let {
                drawIntoCanvas { canvas ->
                    particleSystem.draw(canvas.nativeCanvas)
                }
            }
        }
    )
}