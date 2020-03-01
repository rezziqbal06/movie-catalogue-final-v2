package com.rezziqbal.moviecatalogue.helper

import android.database.Cursor
import android.provider.BaseColumns
import com.rezziqbal.moviecatalogue.db.DatabaseContract
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.M_ID
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.T_ID
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.entity.Tv

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
}