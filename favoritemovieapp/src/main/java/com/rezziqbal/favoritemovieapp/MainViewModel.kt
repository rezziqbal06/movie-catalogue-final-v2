package com.rezziqbal.favoritemovieapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rezziqbal.favoritemovieapp.db.DatabaseContract
import com.rezziqbal.favoritemovieapp.db.DatabaseContract.MovieColumns.Companion.CONTENT_URI_MOVIE
import com.rezziqbal.favoritemovieapp.db.DatabaseContract.TVColumns.Companion.CONTENT_URI_TV
import com.rezziqbal.favoritemovieapp.utils.MappingHelper
import com.rezziqbal.favoritemovieapp.entity.Movie
import com.rezziqbal.favoritemovieapp.entity.Tv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var movieData = MutableLiveData<ArrayList<Movie>>()
    private var tvData = MutableLiveData<ArrayList<Tv>>()

    fun getDataFromDatabase(context: Context) {
        Log.d("action", "ambil data database")
        GlobalScope.launch(Dispatchers.Main) {
            val deferredMovie = async(Dispatchers.IO) {
                val cursor = context.contentResolver?.query(CONTENT_URI_MOVIE, null, null, null, null) as Cursor
                MappingHelper.mapMovieCursorToArray(cursor)
            }
            val movie = deferredMovie.await()
            movieData.value = movie

            val deferredTv = async(Dispatchers.IO) {
                val cursor = context.contentResolver?.query(CONTENT_URI_TV, null, null, null, null) as Cursor
                MappingHelper.mapTvCursorToArray(cursor)
            }
            val tv = deferredTv.await()
            tvData.value = tv

        }
    }

    fun getMovie(): LiveData<ArrayList<Movie>>{
        return movieData
    }

    fun getTv() : LiveData<ArrayList<Tv>>{
        return tvData
    }


    fun removeFavoriteMovie(context: Context, id: String): LiveData<Int>{
        val uriWithId = Uri.parse(CONTENT_URI_MOVIE.toString() + "/" + id)

        val result = MutableLiveData<Int>()
        val values = ContentValues()
        values.put(DatabaseContract.MovieColumns.M_IS_FAVORITE, "tidak")
        result.value = context.contentResolver.update(uriWithId, values, null, null)
        return result
    }

    fun removeFavoriteTv(context: Context, idTv: String): LiveData<Int>{
        val uriWithId = Uri.parse(DatabaseContract.TVColumns.CONTENT_URI_TV.toString() + "/" + idTv)

        val result = MutableLiveData<Int>()
        val values = ContentValues()
        values.put(DatabaseContract.TVColumns.T_IS_FAVORITE, "tidak")
        result.value = context.contentResolver.update(uriWithId, values, null, null)
        return result
    }


}