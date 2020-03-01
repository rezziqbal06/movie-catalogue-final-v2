package com.rezziqbal.moviecatalogue.ui.favorite

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rezziqbal.moviecatalogue.db.DatabaseContract
import com.rezziqbal.moviecatalogue.db.MovieHelper
import com.rezziqbal.moviecatalogue.db.TvHelper
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.entity.Tv

class FavoriteViewModel: ViewModel(){

    private var movie = MutableLiveData<ArrayList<Movie>>()
    private var tv = MutableLiveData<ArrayList<Tv>>()

    fun getMovieByFavorite(context: Context): LiveData<ArrayList<Movie>>{
        val movieHelper = MovieHelper(context)
        movieHelper.open()
        movie.value = movieHelper.getByFavorite("iya")
        movieHelper.close()
        return movie
    }

    fun getTvByFavorite(context: Context): LiveData<ArrayList<Tv>>{
        val tvHelper = TvHelper(context)
        tvHelper.open()
        tv.value = tvHelper.getByFavorite("iya")
        tvHelper.close()
        return tv
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