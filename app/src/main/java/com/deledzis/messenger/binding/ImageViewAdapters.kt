package com.deledzis.messenger.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.deledzis.messenger.R

@BindingAdapter("src_circle")
fun loadCircleImage(view: ImageView, imageUrl: String?) {
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    if (!imageUrl.isNullOrBlank()) {
        Glide.with(view.context)
            .load(imageUrl)
            .thumbnail(0.4f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(RequestOptions().circleCrop())
            .into(view)
    } else {
        Glide.with(view.context)
            .load(R.color.white)
            .thumbnail(0.4f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(RequestOptions().centerCrop())
            .into(view)
    }
}

@BindingAdapter("src")
fun loadImage(view: ImageView, imageUrl: String?) {
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    if (!imageUrl.isNullOrBlank()) {
        if (!(imageUrl.endsWith(".svg") || imageUrl.endsWith(".pdf"))) {
            Glide.with(view.context)
                .load(imageUrl)
                .thumbnail(0.4f)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(RequestOptions().centerCrop())
                .into(view)
        }
    } else {
        Glide.with(view.context)
            .load(R.color.white)
            .thumbnail(0.4f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(RequestOptions().centerCrop())
            .into(view)
    }
}