package com.ivantsov.marsphotos.util.glide

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions


class GlideImageLoader(
    private var mImageView: ImageView,
    private var mProgressBar: ProgressBar,
    private var mPercentLabel: TextView
) {

    fun load(url: String, options: RequestOptions) {
        onConnecting()


        //set Listener & start
        val progressListener = object : ProgressAppGlideModule.UIonProgressListener {
            override fun onProgress(bytesRead: Long, expectedLength: Long) {
                val percent = (100 * bytesRead / expectedLength).toInt() //TODO: magic number
                mProgressBar.progress = percent
                mPercentLabel.text = "$percent%" //TODO: hardcoded text
            }

            override val granualityPercentage: Float = 1.0f //TODO: magic number
        }
        ProgressAppGlideModule.registerListener(url, progressListener)

        //Get Image
        Glide.with(mImageView.context)
            .load(url)
            .transition(withCrossFade())
            .apply(options.skipMemoryCache(true))
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    ProgressAppGlideModule.forget(url)
                    onFinished()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    ProgressAppGlideModule.forget(url)
                    onFinished()
                    return false
                }
            }).into(mImageView)
    }


    private fun onConnecting() {
        mProgressBar.visibility = View.VISIBLE
        mPercentLabel.visibility = View.VISIBLE
    }

    private fun onFinished() {
        mProgressBar.visibility = View.GONE
        mPercentLabel.visibility = View.GONE
        mImageView.visibility = View.VISIBLE
    }
}