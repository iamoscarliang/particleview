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
import com.oscarliang.particleview.core.ParticleConfig
import com.oscarliang.particleview.core.ParticleSystem
import com.oscarliang.particleview.core.model.IntOffset
import com.oscarliang.particleview.core.util.loadBitmap
import kotlinx.coroutines.isActive

/**
 *  Parent view to hold the particle and control the playback of the animation
 *
 * @param modifier - the Modifier to be applied
 * @param configs - the configurations of the animation
 * @param duration - the duration of the animation
 * @param isPause - pause or resume the animation
 * @param isCancel - cancel the animation
 * @param onParticlesEnd - callback being executed when end of animation or cancellation
 */
@Composable
fun ParticleView(
    modifier: Modifier = Modifier,
    configs: List<ParticleConfig>,
    duration: Long,
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
        configs.flatMap { it.images }.forEach {
            bitmapPool.put(
                bitmap = loadBitmap(context, it.imageId),
                id = it.imageId
            )
        }
        isImageLoaded = true
    }

    val particleSystems by rememberSaveable { mutableStateOf(configs.map { ParticleSystem(it) }) }
    var lastFrameMillis by rememberSaveable { mutableLongStateOf(0L) }
    var currentFrameMillis by rememberSaveable { mutableLongStateOf(0L) }

    LaunchedEffect(key1 = isCancel, key2 = isPause) {
        while (isActive) {
            if (isCancel || currentFrameMillis >= duration) {
                particleSystems.forEach { it.release() }
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
                particleSystems.forEach {
                    it.update(
                        currentMillis = currentFrameMillis,
                        elapsedMillis = elapsedMillis,
                        duration = duration
                    )
                }
            }
        }
    }

    Canvas(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                particleSystems.forEach {
                    it.drawArea = IntOffset(coordinates.size.width, coordinates.size.height)
                }
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

            // Trigger redraw when frame change
            currentFrameMillis.let {
                drawIntoCanvas { canvas ->
                    particleSystems.forEach {
                        it.draw(
                            bitmapPool = bitmapPool,
                            canvas = canvas.nativeCanvas,
                            paint = paint,
                            density = density
                        )
                    }
                }
            }
        }
    )

}