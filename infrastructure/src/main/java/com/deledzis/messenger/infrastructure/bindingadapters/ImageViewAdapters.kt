package com.deledzis.messenger.infrastructure.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("android:srcCircle")
fun loadCircleImage(view: ImageView, imageUrl: String?) {
    imageUrl?.run {
        Glide.with(view.context)
            .load(this)
            .thumbnail(0.2f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions().circleCrop())
            .into(view)
    }
}

@BindingAdapter("android:src")
fun loadImage(view: ImageView, imageUrl: String?) {
    imageUrl?.run {
        Glide.with(view.context)
            .load(this)
            .thumbnail(0.2f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }
}