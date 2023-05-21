package com.saurav.sadhgurutv_unofficial.retrofit

import com.saurav.sadhgurutv_unofficial.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var _apiService: ApiInterface? = null

    private fun getRetrofit() =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getApiService(): ApiInterface {
        if (_apiService == null)
            _apiService = getRetrofit().create(ApiInterface::class.java)
        return _apiService!!
    }
}