package com.oscarliang.particleview.xml

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
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

class ParticleView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, style: Int) : super(context, attrs, style)

    private var particleSystem: ParticleSystem? = null
    private var animator: ValueAnimator? = null
    private var duration: Long = 0L
    private var currentMillis = 0L

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
     * @param onParticleClickListener - callback being executed when any particle is clicked
     * @param onAnimationEndListener - callback being executed when end of the animation
     */
    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    fun start(
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
        onParticleClickListener: (Particle) -> Unit = {},
        onAnimationEndListener: () -> Unit = {},
    ) {
        this.duration = duration
        this.currentMillis = 0

        val particleSystem = ParticleSystem(
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
            density = resources.displayMetrics.density
        )
        this.particleSystem = particleSystem

        animator?.cancel()
        val animator = ValueAnimator.ofInt(0, duration.toInt())
            .apply {
                this.duration = duration
                this.interpolator = LinearInterpolator()
                addListener(onEnd = {
                    particleSystem.release()
                    this@ParticleView.particleSystem = null
                    onAnimationEndListener()
                })
                addUpdateListener { animation ->
                    val totalMillis = animation.animatedValue as Int
                    val elapsedMillis = totalMillis - currentMillis
                    currentMillis = totalMillis.toLong()
                    particleSystem.update(currentMillis, elapsedMillis)
                    postInvalidate()
                }
            }
        animator.start()
        this.animator = animator

        // Register the layout listener to get the real width and height
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                particleSystem.width = width
                particleSystem.height = height
            }
        })

        // Add touch listener to get particle click callback
        setOnTouchListener { _, event ->
            val action = event.actionMasked
            if (action == MotionEvent.ACTION_DOWN) {
                val selected = particleSystem.getParticleAt(event.x, event.y)
                if (selected != null) {
                    onParticleClickListener(selected)
                }
            }
            true
        }
    }

    fun cancel() {
        currentMillis = duration
        animator?.currentPlayTime = duration
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        particleSystem?.draw(canvas)
    }

}