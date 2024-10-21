package com.oscarliang.particleview.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun loadBitmap(context: Context, id: Int): Bitmap = suspendCoroutine {
    Glide.with(context).asBitmap().load(id).into(
        object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                it.resume(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        }
    )
}
