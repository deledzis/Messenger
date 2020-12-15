package com.deledzis.messenger.util

import android.graphics.Bitmap
import android.graphics.Paint
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.request.RequestOptions
import java.security.MessageDigest

fun <T> RequestBuilder<T>.resize(maxWidth: Int) =
    apply(RequestOptions().transform(BigBitmapTransformation(maxWidth)))

class BigBitmapTransformation(private val maxWidth: Int) : BitmapTransformation() {
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return if (toTransform.height <= maxWidth) {
            TransformationUtils.centerCrop(
                pool,
                toTransform,
                toTransform.width,
                toTransform.height
            )
//            toTransform
        } else TransformationUtils.centerCrop(
            pool,
            toTransform,
            toTransform.width * maxWidth / toTransform.height,
            maxWidth
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is BigBitmapTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    companion object {
        private const val ID = "BigBitmapTransformation"
        private val ID_BYTES = ID.toByteArray(CHARSET)
        private val DEFAULT_PAINT: Paint = Paint(TransformationUtils.PAINT_FLAGS)
    }
}