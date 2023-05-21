package com.saurav.sadhgurutv_unofficial

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.saurav.sadhgurutv_unofficial.bean.Video
import com.saurav.sadhgurutv_unofficial.retrofit.Constants.MOVIE
import com.saurav.sadhgurutv_unofficial.retrofit.Constants.UPDATE
import com.saurav.sadhgurutv_unofficial.retrofit.Constants.UPDATE_CTA
import com.saurav.sadhgurutv_unofficial.retrofit.Constants.VERSION
import com.saurav.sadhgurutv_unofficial.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainFragment : BrowseSupportFragment() {
    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private var bgImgs: List<String> = emptyList()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareBackgroundManager()
        setupUIElements()
        setupEventListeners()
        getData()
    }

    override fun onResume() {
        super.onResume()
        if (bgImgs.isNotEmpty())
            updateBackground(bgImgs.random())
    }

    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(requireActivity().window)
        mDefaultBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
        mMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        headersState = BrowseSupportFragment.HEADERS_HIDDEN
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireContext(), R.color.fastlane_background)
    }

    private fun getData() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            Toast.makeText(context, "Please check sometime later, API Error", Toast.LENGTH_LONG).show()
        }
        lifecycleScope.launch(exceptionHandler) {
            RetrofitClient.getApiService().getMainResponse()?.takeIf {
                it.isSuccessful && it.body() != null && it.body()!!.videos.isNullOrEmpty().not()
            }?.body()?.apply {

                    /*
                    - publish apk in git,
                    - put link of apk on site: apk has site link already /?page=sgtv ,
                    - make qr video -> my site blog(& URL decide) on how to install, & checkout more projects claim & follow on IG,linkedin.. -> git url/repo.

                    - more colourful, more than asked(needed) give..
                    - i can play with the world, by tech -> create media routes n self publish & give utils to people..
                    THIS FREE MEI SABKO ACCESS KRNA IS GEM AVAILABLE ONLY TO AN ENGINEER, & IS ENTREPRENEUR BIRTHER.

                    - DUMP VIDEOS 1 PLACE, NOT IN API WALI JAGAH..
                    & SAME SERIALLY.
                     */
                bgImgs = bg
                updateBackground(bg.random())
                videos as MutableList
                videos.shuffle()
                if(true|| latest > VERSION){
                    Toast.makeText(context, UPDATE_CTA, Toast.LENGTH_LONG).show()
                    videos.add(0, Video(UPDATE, UPDATE_CTA))
                }

                loadRows(videos)
            }
        }
    }

    private fun loadRows(list: List<Video>) {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()
        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        listRowAdapter.addAll(0, list.mapIndexed{ i,v-> v.copy(pos = i) })
        val header = HeaderItem(0, "Greatest of All Time")
        rowsAdapter.add(ListRow(header, listRowAdapter))
        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is Video) {
                if(item.v!=UPDATE_CTA) {
                    val intent = Intent(context!!, PlaybackActivity::class.java)
                    intent.putExtra(MOVIE, item)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, UPDATE_CTA, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateBackground(uri: String?) {
        val width = mMetrics.widthPixels
        val height = mMetrics.heightPixels
        Glide.with(requireContext())
            .load(uri)
            .centerCrop()
            .error(mDefaultBackground)
            .into<SimpleTarget<Drawable>>(
                object : SimpleTarget<Drawable>(width, height) {
                    override fun onResourceReady(
                        drawable: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        mBackgroundManager.drawable = drawable
                    }
                })
    }
}