package com.saurav.sadhgurutv_unofficial

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saurav.sadhgurutv_unofficial.bean.Video
import com.saurav.sadhgurutv_unofficial.glide.ColorFilterTransformation
import com.saurav.sadhgurutv_unofficial.retrofit.Constants.SG_THUMB
import com.saurav.sadhgurutv_unofficial.retrofit.Constants.UPDATE
import com.saurav.sadhgurutv_unofficial.retrofit.Constants.UPGRADE_THUMB
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class CardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {

        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val movie = item as Video
        val cardView = viewHolder.view as ImageCardView

        if (movie.v != null) {
            cardView.titleText = movie.t
            cardView.contentText = "Sadhguru Jaggi Vasudev"
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
            Glide.with(viewHolder.view.context)
                .load(if (movie.t != UPDATE) SG_THUMB else UPGRADE_THUMB)
                .centerCrop()
                .apply {
                    if (movie.t != UPDATE){
                        apply(RequestOptions.bitmapTransform(ColorFilterTransformation(
                            (if(movie.pos%2==0) Color.CYAN else Color.MAGENTA).addAlphaToColor(60)
                        )))
                    }


                }
                .error(mDefaultCardImage)
                .into(cardView.mainImageView)
        }
    }

    private fun Int.addAlphaToColor(alpha: Int): Int {
        val red = Color.red(this)
        val green = Color.green(this)
        val blue = Color.blue(this)
        return Color.argb(alpha, red, green, blue)
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        // Both background colors should be set because the view"s background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private val CARD_WIDTH = 626 //313
        private val CARD_HEIGHT = 352 //176
    }
}