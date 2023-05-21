package com.saurav.sadhgurutv_unofficial

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow
import com.saurav.sadhgurutv_unofficial.bean.Video
import com.saurav.sadhgurutv_unofficial.retrofit.Constants.MOVIE

class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (title, videoUrl) =
            activity?.intent?.getSerializableExtra(MOVIE) as Video

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        val playerAdapter = MediaPlayerAdapter(context)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)

        mTransportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.title = title
        mTransportControlGlue.subtitle = "Android App Made by SauravC."
        mTransportControlGlue.playWhenPrepared()

        try {
            playerAdapter.setDataSource(Uri.parse(videoUrl))
        } catch (e: Exception) {
            Toast.makeText(context, "SOME ERROR OCCURED", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }

}