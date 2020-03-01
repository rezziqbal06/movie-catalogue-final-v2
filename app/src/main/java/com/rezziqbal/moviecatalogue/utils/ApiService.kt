package com.rezziqbal.moviecatalogue.utils

import com.rezziqbal.moviecatalogue.response.MovieDataResponse
import com.rezziqbal.moviecatalogue.response.TvDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie/")
    fun getMovies(@Query("api_key") api_key: String): Call<MovieDataResponse>
    @GET("search/movie")
    fun searchMovies(@Query("api_key") api_key: String, @Query("query") query: String): Call<MovieDataResponse>
    @GET("discover/movie/")
    fun getMovieRelease(@Query("api_key") api_key: String,
                        @Query("primary_release_date.gte") gte: String,
                        @Query("primary_release_date.lte") lte: String): Call<MovieDataResponse>
    @GET("discover/tv/")
    fun getTv(@Query("api_key") api_key: String): Call<TvDataResponse>
    @GET("search/tv")
    fun searchTvs(@Query("api_key") api_key: String, @Query("query") query: String): Call<TvDataResponse>
}