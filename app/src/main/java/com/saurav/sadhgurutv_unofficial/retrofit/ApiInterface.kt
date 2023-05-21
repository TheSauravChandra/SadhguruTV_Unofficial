package com.saurav.sadhgurutv_unofficial.retrofit

import com.saurav.sadhgurutv_unofficial.bean.VideoResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("response.json")
    suspend fun getMainResponse(): Response<VideoResponse>
}