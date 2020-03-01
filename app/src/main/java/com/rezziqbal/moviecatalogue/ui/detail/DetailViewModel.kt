package com.rezziqbal.moviecatalogue.ui.detail

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rezziqbal.moviecatalogue.db.DatabaseContract
import com.rezziqbal.moviecatalogue.db.MovieHelper
import com.rezziqbal.moviecatalogue.db.TvHelper
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.entity.Tv
import com.rezziqbal.moviecatalogue.helper.MappingHelper

class DetailViewModel: ViewModel(){
    private val movie =  MutableLiveData<Movie>()
    private val tv = MutableLiveData<Tv>()

    fun getDetailMovie(context: Context,movie: Movie) : LiveData<Movie>{
        val movieHelper = MovieHelper(context)
        movieHelper.open()
        var cursor = movieHelper.getById(movie.id.toString())
        Log.d("cursor ", cursor.toString())
        if(cursor != null && cursor.moveToFirst()){
            this.movie.value = MappingHelper.mapMovieCursorToObject(cursor)
            cursor.close()
        }else{
            val result = movieHelper.insert(movie)
            if(result > 0){
                cursor = movieHelper.getById(movie.id.toString())
                if(cursor != null && cursor.moveToFirst()){
                    this.movie.value = MappingHelper.mapMovieCursorToObject(cursor)
                    cursor.close()
                }
            }
        }
        movieHelper.close()
        return this.movie
    }

    fun getDetailTv(context: Context,tv: Tv) : LiveData<Tv>{
        val tvHelper = TvHelper(context)
        tvHelper.open()
        var cursor = tvHelper.getById(tv.id.toString())
        if(cursor != null && cursor.moveToFirst()){
            this.tv.value = MappingHelper.mapTvCursorToObject(cursor)
            cursor.close()
        }else{
            val result = tvHelper.insert(tv)
            if(result > 0){
                cursor = tvHelper.getById(tv.id.toString())
                if(cursor != null && cursor.moveToFirst()){
                    this.tv.value = MappingHelper.mapTvCursorToObject(cursor)
                    cursor.close()
                }
            }
        }
        tvHelper.close()
        return this.tv
    }
    
    fun addFavoriteTv(context: Context, id: String): LiveData<Int>{
        val result = MutableLiveData<Int>()
        val tvHelper = TvHelper(context)
        val values = ContentValues()
        values.put(DatabaseContract.TVColumns.T_IS_FAVORITE, "iya")
        tvHelper.open()
        result.value = tvHelper.update(id, values)
        tvHelper.close()
        
        return result
    }
    
    fun removeFavoriteTv(context: Context, idTv: String): LiveData<Int>{
        val result = MutableLiveData<Int>()
        val tvHelper = TvHelper(context)
        val values = ContentValues()
        values.put(DatabaseContract.TVColumns.T_IS_FAVORITE, "tidak")
        tvHelper.open()
        result.value = tvHelper.update(idTv, values)
        tvHelper.close()
        
        return result
    }
    
    fun addFavoriteMovie(context: Context, id: String): LiveData<Int>{
        val result = MutableLiveData<Int>()
        val movieHelper = MovieHelper(context)
        val values = ContentValues()
        values.put(DatabaseContract.MovieColumns.M_IS_FAVORITE, "iya")
        movieHelper.open()
        result.value = movieHelper.update(id, values)
        movieHelper.close()
        
        return result
    }

    fun removeFavoriteMovie(context: Context, id: String): LiveData<Int>{
        val result = MutableLiveData<Int>()
        val movieHelper = MovieHelper(context)
        val values = ContentValues()
        values.put(DatabaseContract.MovieColumns.M_IS_FAVORITE, "tidak")
        movieHelper.open()
        result.value = movieHelper.update(id, values)
        movieHelper.close()

        return result
    }
}