package com.deledzis.messenger.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.deledzis.messenger.util.resize

@BindingAdapter("app:src")
fun loadImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(view.context)
            .load(it)
            .resize(400)
            .thumbnail(0.4f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(view)
    }
}