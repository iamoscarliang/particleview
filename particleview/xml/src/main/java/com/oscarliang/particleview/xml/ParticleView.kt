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
import com.oscarliang.particleview.core.DEFAULT_COUNT_PER_SECOND
import com.oscarliang.particleview.core.DEFAULT_DURATION
import com.oscarliang.particleview.core.DEFAULT_FADE_OUT_DURATION
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
     * @param countPerSecond - the count of particle emit per second
     * @param duration - the duration of the animation
     * @param fadeOutDuration - the fade out duration at the end of animation
     * @param fadeOutEnable - controls the fade out effect at the end of animation. When false,
     * the particle will disappear immediately and the fadeOutDuration has no effect
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
        countPerSecond: Int = DEFAULT_COUNT_PER_SECOND,
        duration: Long = DEFAULT_DURATION,
        fadeOutDuration: Long = DEFAULT_FADE_OUT_DURATION,
        fadeOutEnable: Boolean = true,
        onParticleClickListener: (Particle) -> Unit = {},
        onAnimationEndListener: () -> Unit = {},
    ) {
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
            countPerSecond = countPerSecond,
            duration = duration,
            fadeOutDuration = fadeOutDuration,
            fadeOutEnable = fadeOutEnable,
            density = resources.displayMetrics.density
        )

        animator?.cancel()
        val totalDuration = duration + if (fadeOutEnable) fadeOutDuration else 0
        val animator = ValueAnimator.ofInt(0, totalDuration.toInt())
            .apply {
                this.duration = totalDuration
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

        this.particleSystem = particleSystem
        this.animator = animator
        this.duration = duration
        this.currentMillis = 0

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

    /**
     * Cancel the animation. The animation will jump to the end and play the
     * fade out effect dependent on the fadeOutEnable is true or not
     */
    fun cancel() {
        currentMillis = duration
        animator?.currentPlayTime = duration
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        particleSystem?.draw(canvas)
    }

}