package com.deledzis.messenger.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("app:src")
fun loadImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(view.context)
            .load(it)
            .thumbnail(0.2f)
//            .resize(400)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }
}