package com.rezziqbal.favoritemovieapp.utils

import android.database.Cursor
import com.rezziqbal.favoritemovieapp.db.DatabaseContract
import com.rezziqbal.favoritemovieapp.db.DatabaseContract.MovieColumns.Companion.M_ID
import com.rezziqbal.favoritemovieapp.db.DatabaseContract.TVColumns.Companion.T_ID
import com.rezziqbal.favoritemovieapp.entity.Movie
import com.rezziqbal.favoritemovieapp.entity.Tv

object MappingHelper {
    fun mapMovieCursorToObject(cursor: Cursor): Movie {
        cursor.moveToFirst()
        val id =  cursor.getLong(cursor.getColumnIndexOrThrow(M_ID))
        val nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MNAMA))
        val deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MDESKRIPSI))
        val poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MPOSTER))
        val rdate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MRDATE))
        val vote = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MVOTE))
        val backdrop = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MBACKDROP))
        val is_favorite = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.M_IS_FAVORITE))
        return Movie(id, nama, rdate, vote, deskripsi, poster, backdrop, is_favorite)
    }

    fun mapMovieCursorToArray(cursor: Cursor): ArrayList<Movie> {
        val movieList = ArrayList<Movie>()
        cursor.moveToFirst()
        while (cursor.moveToNext()){
            val id =  cursor.getLong(cursor.getColumnIndexOrThrow(M_ID))
            val nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MNAMA))
            val deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MDESKRIPSI))
            val poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MPOSTER))
            val rdate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MRDATE))
            val vote = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MVOTE))
            val backdrop = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.MBACKDROP))
            movieList.add(Movie(id, nama, rdate, vote, deskripsi, poster, backdrop))
        }

        return movieList
    }

    fun mapTvCursorToObject(cursor: Cursor): Tv {
        cursor.moveToFirst()
        val id =  cursor.getLong(cursor.getColumnIndexOrThrow(T_ID))
        val nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TNAMA))
        val deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TDESKRIPSI))
        val poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TPOSTER))
        val rdate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TRDATE))
        val vote = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TVOTE))
        val backdrop = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TBACKDROP))
        val is_favorite = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.T_IS_FAVORITE))
        return Tv(id, nama, rdate, vote, deskripsi, poster, backdrop, is_favorite)
    }

    fun mapTvCursorToArray(cursor: Cursor): ArrayList<Tv> {
        val tvList = ArrayList<Tv>()
        cursor.moveToFirst()
        while (cursor.moveToNext()){
            val id =  cursor.getLong(cursor.getColumnIndexOrThrow(T_ID))
            val nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TNAMA))
            val deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TDESKRIPSI))
            val poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TPOSTER))
            val rdate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TRDATE))
            val vote = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TVOTE))
            val backdrop = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TBACKDROP))
            tvList.add(Tv(id, nama, rdate, vote, deskripsi, poster, backdrop))
        }
        return tvList
    }
}