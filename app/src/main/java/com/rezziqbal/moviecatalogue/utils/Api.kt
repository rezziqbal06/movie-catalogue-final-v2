package com.rezziqbal.moviecatalogue.utils

import com.google.gson.GsonBuilder
import com.rezziqbal.moviecatalogue.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    companion object{
        val gson = GsonBuilder().create()

        private val retrofit = Retrofit.Builder()
            .baseUrl(Cons.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val service = retrofit.create(ApiService::class.java)
    }


}