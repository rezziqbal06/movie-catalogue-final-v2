package com.rezziqbal.moviecatalogue.ui.movie

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rezziqbal.moviecatalogue.db.MovieHelper
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.response.MovieDataResponse
import com.rezziqbal.moviecatalogue.response.MovieResponse
import com.rezziqbal.moviecatalogue.utils.Api
import com.rezziqbal.moviecatalogue.utils.Cons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private var movie = MutableLiveData<MovieDataResponse>()
    private var movieData = MutableLiveData<ArrayList<Movie>>()

    fun getMovie(): LiveData<MovieDataResponse>{
        Api.service.getMovies(Cons.apikey).enqueue(object:Callback<MovieDataResponse>{
            override fun onFailure(call: Call<MovieDataResponse>, t: Throwable) {
                movie.value = null
                Log.d("status1", t.message)
            }

            override fun onResponse(call: Call<MovieDataResponse>, dataResponse: Response<MovieDataResponse>) {
                if(dataResponse.isSuccessful){
                    Log.d("status2", dataResponse.message())
                    movie.value = dataResponse.body()
                    Log.d("isi2", dataResponse.body()?.result.toString())
                }else{
                    Log.d("status3", dataResponse.message())
                    movie.value = null
                }
            }

        })
        return movie
    }

    fun getDataFromDatabase(context: Context) : LiveData<ArrayList<Movie>>{
        val movieHelper = MovieHelper(context)
        movieHelper.open()
        movieData.value = movieHelper.getAll()
        movieHelper.close()
        return movieData
    }

    fun insertDataToDatabase(context: Context, arrayMovieResponse: ArrayList<MovieResponse>): MutableLiveData<ArrayList<Movie>>{
        val movieHelper = MovieHelper(context)
        movieHelper.open()
        try{
            movieHelper.beginTransaction()
            for(movies in arrayMovieResponse){
                movieHelper.insertTransaction(movies)
            }
            movieHelper.setTransactionSuccessfull()
        }catch (e: Exception){

        }finally {
            movieHelper.endTransaction()
        }
        movieData.value = movieHelper.getAll()
        movieHelper.close()
        return movieData
    }

    fun deleteItem(context: Context, id: String): LiveData<Int>{
        val result = MutableLiveData<Int>()
        val movieHelper = MovieHelper(context)
        movieHelper.open()
        result.value = movieHelper.delete(id)
        movieHelper.close()
        return result
    }

    fun searchByApi(nama: String): LiveData<MovieDataResponse>{
        Api.service.searchMovies(Cons.apikey, nama).enqueue(object: Callback<MovieDataResponse>{
            override fun onFailure(call: Call<MovieDataResponse>, t: Throwable) {
                Log.d("request", call.request().toString())
                movie.value = null
                Log.d("failed", t.message)
            }

            override fun onResponse(call: Call<MovieDataResponse>, dataResponse: Response<MovieDataResponse>) {
                Log.d("request", call.request().toString())
                if(dataResponse.isSuccessful){
                    Log.d("status2", dataResponse.message())
                    movie.value = dataResponse.body()
                }else{
                    Log.d("status3", dataResponse.message())
                    movie.value = null
                }
            }

        })
        return movie
    }
}