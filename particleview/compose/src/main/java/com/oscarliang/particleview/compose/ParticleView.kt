package com.oscarliang.particleview.compose

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import com.oscarliang.particleview.core.BitmapPool
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
import com.oscarliang.particleview.core.ParticleSystem
import com.oscarliang.particleview.core.model.FloatOffset
import com.oscarliang.particleview.core.model.Image
import com.oscarliang.particleview.core.model.IntOffset
import com.oscarliang.particleview.core.util.loadBitmap
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
 * @param isPause - pause or resume the animation
 * @param isCancel - cancel the animation
 * @param onParticlesEnd - callback being executed when end of animation or cancellation
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
    isPause: Boolean = false,
    isCancel: Boolean = false,
    onParticlesEnd: () -> Unit = {}
) {

    val context = LocalContext.current
    val density = LocalDensity.current.density

    val bitmapPool by remember { mutableStateOf(BitmapPool()) }
    val paint by remember { mutableStateOf(Paint()) }
    var isImageLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        images.forEach {
            bitmapPool.put(
                image = loadBitmap(context, it.imageId),
                id = it.imageId
            )
        }
        isImageLoaded = true
    }

    val particleSystem by rememberSaveable {
        mutableStateOf(
            ParticleSystem(
                images = images,
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
    var lastFrameMillis by rememberSaveable { mutableLongStateOf(0L) }
    var currentFrameMillis by rememberSaveable { mutableLongStateOf(0L) }

    LaunchedEffect(key1 = isCancel, key2 = isPause) {
        while (isActive) {
            if (isCancel || currentFrameMillis >= duration) {
                particleSystem.release()
                onParticlesEnd()
                break
            }
            if (isPause) {
                // Reset last frame time, so when resume the
                // animation will start from where it pause.
                lastFrameMillis = 0L
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
            .onGloballyPositioned { coordinates ->
                particleSystem.drawArea = IntOffset(coordinates.size.width, coordinates.size.height)
            },
        onDraw = {
            // Since the image is being loaded asynchronously,
            // we redraw the canvas after loading complete.
            // We make sure draw the canvas at least 1 time,
            // so when pausing or config change the current
            // state of the canvas will be remain.
            if (!isImageLoaded) {
                return@Canvas
            }

            // We trigger redraw when frame change
            currentFrameMillis.let {
                drawIntoCanvas { canvas ->
                    particleSystem.draw(
                        bitmapPool = bitmapPool,
                        canvas = canvas.nativeCanvas,
                        paint = paint
                    )
                }
            }
        }
    )

}