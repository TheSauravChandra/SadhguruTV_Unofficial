package com.saurav.sadhgurutv_unofficial.bean

data class VideoResponse(
    val downloadUrl: String? = null,
    val latest: Int = 1,
    val videos: List<Video>? = null,
    val bg: List<String>
)