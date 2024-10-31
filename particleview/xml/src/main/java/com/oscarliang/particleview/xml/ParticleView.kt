package com.oscarliang.particleview.xml

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import com.oscarliang.particleview.core.BitmapPool
import com.oscarliang.particleview.core.ParticleConfig
import com.oscarliang.particleview.core.ParticleSystem
import com.oscarliang.particleview.core.model.IntOffset
import com.oscarliang.particleview.core.util.loadBitmap

class ParticleView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, style: Int) : super(context, attrs, style)

    private var particleSystems: List<ParticleSystem>? = null
    private var animator: ValueAnimator? = null
    private var currentMillis = 0L

    private val bitmapPool = BitmapPool()
    private val paint = Paint()

    /**
     *  Parent view to hold the particle and control the playback of the animation
     *
     * @param configs - the configurations of the animation
     * @param duration - the duration of the animation
     * @param onParticlesEnd - callback being executed when end of animation or cancellation
     */
    suspend fun start(
        configs: List<ParticleConfig>,
        duration: Long,
        onParticlesEnd: () -> Unit = {}
    ) {
        particleSystems = configs.map { ParticleSystem((it)) }
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
                    particleSystems?.forEach {
                        it.update(
                            currentMillis = currentMillis,
                            elapsedMillis = elapsedMillis,
                            duration = duration
                        )
                    }
                    postInvalidate()
                }
                start()
            }

        // Register the layout listener to get the real width and height
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                particleSystems?.forEach { it.drawArea = IntOffset(width, height) }
            }
        })

        configs.flatMap { it.images }.forEach {
            bitmapPool.put(
                bitmap = loadBitmap(context, it.imageId),
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
        particleSystems?.forEach { it.release() }
        particleSystems = null
        animator = null
        currentMillis = 0L
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        particleSystems?.forEach {
            it.draw(
                bitmapPool = bitmapPool,
                canvas = canvas,
                paint = paint,
                density = resources.displayMetrics.density
            )
        }
    }

}