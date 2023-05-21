package com.saurav.sadhgurutv_unofficial.glide

import android.graphics.*
import androidx.core.graphics.alpha
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest

class ColorFilterTransformation(private val color: Int) : BitmapTransformation() {

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        val colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        paint.colorFilter = colorFilter

        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + color).toByteArray(Charset.forName("UTF-8")))
    }

    override fun equals(other: Any?): Boolean {
        return other is ColorFilterTransformation && other.color == color
    }

    override fun hashCode(): Int {
        return ID.hashCode() + color
    }

    companion object {
        private const val ID = "com.example.ColorFilterTransformation"
    }
}