package com.rezziqbal.favoritemovieapp.ui.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rezziqbal.favoritemovieapp.db.DatabaseContract.MovieColumns.Companion.CONTENT_URI_MOVIE
import com.rezziqbal.favoritemovieapp.db.DatabaseContract.TVColumns.Companion.CONTENT_URI_TV
import com.rezziqbal.favoritemovieapp.utils.MappingHelper
import com.rezziqbal.favoritemovieapp.entity.Movie
import com.rezziqbal.favoritemovieapp.entity.Tv

class DetailViewModel: ViewModel(){
    private val movie =  MutableLiveData<Movie>()
    private val tv = MutableLiveData<Tv>()

    fun getDetailMovie(context: Context,movie: Movie) : LiveData<Movie>{
        val uriWithId = Uri.parse(CONTENT_URI_MOVIE.toString()+"/"+movie.id.toString())
        var cursor = context.contentResolver.query(uriWithId, null, null, null, null)
        if(cursor != null && cursor.moveToFirst()){
            this.movie.value = MappingHelper.mapMovieCursorToObject(cursor)
            cursor.close()
        }
        return this.movie
    }

    fun getDetailTv(context: Context,tv: Tv) : LiveData<Tv>{
        val uriWithId = Uri.parse(CONTENT_URI_TV.toString() + "/"+ tv.id.toString())
        var cursor = context.contentResolver.query(uriWithId, null, null, null, null)
        if(cursor != null && cursor.moveToFirst()){
            this.tv.value = MappingHelper.mapTvCursorToObject(cursor)
            cursor.close()
        }
        return this.tv
    }
    

}