package com.exercise.digio.data.network.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitInstance @Inject constructor() {
    private val baseUrl = "https://ext.digio.in:444/"

    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getRetrofit(): APIService = retrofit.create(APIService::class.java)

}