package com.codequark.imagemanager

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.codequark.imagemanager.interfaces.ImageListener
import java.io.File

@Suppress("unused")
class ImageManager {
    private val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .override(Target.SIZE_ORIGINAL)
            .dontTransform()

    private lateinit var thumbRequest: RequestBuilder<Drawable>
    private lateinit var errorRequest: RequestBuilder<Drawable>

    companion object {
        var instance = ImageManager()
    }

    fun initialize(@NonNull application: Application) {
        this.errorRequest = Glide.with(application.applicationContext)
                .load(R.drawable.ic_error)
                .apply(options)

        this.thumbRequest = Glide.with(application.applicationContext)
                .load(R.drawable.ic_loading)
                .apply(options)
    }

    fun setImage(@NonNull file: File, @NonNull image: ImageView) {
        Glide.with(image)
                .load(file)
                .apply(options)
                .thumbnail(thumbRequest)
                .error(errorRequest)
                .into(image)
    }

    fun setImage(@NonNull file: File, @NonNull image: ImageView, @NonNull x: Int, @NonNull y: Int) {
        //LogUtils.print("Trying to override to: $x x $y")

        Glide.with(image)
                .load(file)
                .apply(options)
                .thumbnail(thumbRequest)
                .error(errorRequest)
                //.override(x, y)
                .into(image)
    }

    fun setImage(@NonNull url: String, @NonNull image: ImageView) {
        Glide.with(image)
                .load(url)
                .apply(options)
                .thumbnail(thumbRequest)
                .error(errorRequest)
                .into(image)
    }

    fun setImage(@NonNull bitmap: Bitmap, @NonNull image: ImageView) {
        Glide.with(image)
                .load(bitmap)
                .apply(options)
                .thumbnail(thumbRequest)
                .error(errorRequest)
                .into(image)
    }

    fun setImage(@NonNull resource: Int, @NonNull image: ImageView) {
        Glide.with(image)
                .load(resource)
                .apply(options)
                .thumbnail(thumbRequest)
                .error(errorRequest)
                .into(image)
    }

    fun setImage(@NonNull url: String, @NonNull image: ImageView, @NonNull listener: ImageListener) {
        Glide.with(image)
                .asBitmap()
                .apply(options)
                .load(url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                        listener.onBitmapLoaded(bitmap)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
    }

    fun setImage(@NonNull context: Context, @NonNull url: String, @NonNull listener: ImageListener) {
        GlideApp.with(context)
                .load(url)
                .apply(options)
                .thumbnail(thumbRequest)
                .error(errorRequest)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                        listener.onDrawableLoaded(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
    }
}