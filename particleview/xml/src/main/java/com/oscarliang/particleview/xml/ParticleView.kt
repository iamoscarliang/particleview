package com.oscarliang.particleview.xml

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
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

class ParticleView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, style: Int) : super(context, attrs, style)

    private var particleSystem: ParticleSystem? = null
    private var animator: ValueAnimator? = null
    private var currentMillis = 0L

    private val bitmapPool = BitmapPool()
    private val paint = Paint()

    /**
     *  Create and start the animation. Cancel the previous
     *  animation if already running and start the new one.
     *
     * @param images - the image of the particle
     * @param startX - the start x position of the particle. Range from [0.0f, 1.0f] relative to the view's width
     * @param startY - the start y position of the particle. Range from [0.0f, 1.0f] relative to the view's height
     * @param speed - the speed of the particle moving in (px / per second)
     * @param accelX - the x acceleration added to the speed per second
     * @param accelY - the y acceleration added to the speed per second
     * @param angle - the direction of the particle's speed. Range from [0, 360]
     * Top - 180, Right - 90, Bottom - 0, Left - 270
     * @param rotation - the start rotation of the particle. Range from [0, 360]
     * @param rotationSpeed - the rotation speed of the particle rotating in (degree / per second)
     * @param particleDuration - the duration of the individual particle
     * @param particleFadeOutDuration - the duration of the fade out effect of particle
     * @param particlePerSecond - the amount of particle being emitted per second
     * @param duration - the duration of the animation
     * @param onParticlesEnd - callback being executed when end of animation or cancellation
     */
    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    suspend fun start(
        images: List<Image>,
        startX: FloatOffset = DEFAULT_START_X,
        startY: FloatOffset = DEFAULT_START_Y,
        angle: IntOffset = DEFAULT_ANGLE,
        speed: FloatOffset = DEFAULT_SPEED,
        accelX: FloatOffset = DEFAULT_ACCEL_X,
        accelY: FloatOffset = DEFAULT_ACCEL_Y,
        rotation: IntOffset = DEFAULT_ROTATION,
        rotationSpeed: FloatOffset = DEFAULT_ROTATION_SPEED,
        particleDuration: Long = DEFAULT_PARTICLE_DURATION,
        particleFadeOutDuration: Long = DEFAULT_PARTICLE_FADE_OUT_DURATION,
        particlePerSecond: Int = DEFAULT_PARTICLE_PER_SECOND,
        duration: Long = DEFAULT_DURATION,
        onParticlesEnd: () -> Unit = {}
    ) {

        particleSystem = ParticleSystem(
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
            density = resources.displayMetrics.density
        )

        animator = ValueAnimator.ofInt(0, duration.toInt())
            .apply {
                this.duration = duration
                this.interpolator = LinearInterpolator()
                this.currentPlayTime = currentMillis
                addListener(object : AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                    }

                    override fun onAnimationEnd(p0: Animator) {
                        onParticlesEnd()
                        release()
                        invalidate()
                    }

                    override fun onAnimationCancel(p0: Animator) {
                        onParticlesEnd()
                        release()
                        invalidate()
                    }

                    override fun onAnimationRepeat(p0: Animator) {
                    }
                })
                addUpdateListener { animation ->
                    val totalMillis = animation.animatedValue as Int
                    val elapsedMillis = totalMillis - currentMillis
                    currentMillis = totalMillis.toLong()
                    particleSystem?.update(currentMillis, elapsedMillis)
                    postInvalidate()
                }
                start()
            }

        // Register the layout listener to get the real width and height
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                particleSystem?.drawArea = IntOffset(width, height)
            }
        })

        images.forEach {
            bitmapPool.put(
                image = loadBitmap(context, it.imageId),
                id = it.imageId
            )
        }
    }

    fun pause() {
        animator?.pause()
    }

    fun resume() {
        animator?.resume()
    }

    fun cancel() {
        animator?.cancel()
    }

    fun isPause() = animator?.isPaused ?: false

    fun isRunning() = animator?.isRunning ?: false

    private fun release() {
        particleSystem?.release()
        particleSystem = null
        animator = null
        currentMillis = 0L
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        particleSystem?.draw(
            bitmapPool = bitmapPool,
            canvas = canvas,
            paint = paint
        )
    }

}