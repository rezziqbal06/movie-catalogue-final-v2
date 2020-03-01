package com.rezziqbal.moviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.rezziqbal.moviecatalogue.db.DatabaseContract.AUTHORITY
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.CONTENT_URI_MOVIE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TABLE_MOVIE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TABLE_TV
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.CONTENT_URI_TV
import com.rezziqbal.moviecatalogue.db.MovieHelper
import com.rezziqbal.moviecatalogue.db.TvHelper

class AppProvider : ContentProvider() {
    companion object {
        private const val MOVIE = 1
        private const val MOVIE_ID = 2
        private const val TV = 3
        private const val TV_ID = 4
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var movieHelper: MovieHelper
        private lateinit var tvHelper: TvHelper
        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE)
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_MOVIE/#",
                MOVIE_ID)

            sUriMatcher.addURI(AUTHORITY, TABLE_TV, TV)
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_TV/#",
                TV_ID)
        }
    }
    override fun onCreate(): Boolean {
        tvHelper = TvHelper.getInstance(context as Context)
        tvHelper.open()
        movieHelper = MovieHelper.getInstance(context as Context)
        movieHelper.open()
        return true
    }

    //query favorite movie
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            MOVIE -> cursor = movieHelper.queryByFavorite("iya")
            MOVIE_ID -> cursor = movieHelper.getById(uri.lastPathSegment.toString())
            TV -> cursor = tvHelper.queryByFavorite("iya")
            TV_ID -> cursor = tvHelper.getById(uri.lastPathSegment.toString())
            else -> cursor = null
        }

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long?
        val resultUri: Uri?
        when (sUriMatcher.match(uri)) {
            MOVIE -> {
                added = movieHelper.insertValue(values)
                context?.contentResolver?.notifyChange(CONTENT_URI_MOVIE, null)
                resultUri = Uri.parse("$CONTENT_URI_MOVIE/$added")
            }
            TV -> {
                added = tvHelper.insertValue(values)
                context?.contentResolver?.notifyChange(CONTENT_URI_TV, null)
                resultUri = Uri.parse("$CONTENT_URI_TV/$added")
            }
            else -> {
                added = 0
                context?.contentResolver?.notifyChange(CONTENT_URI_MOVIE, null)
                resultUri = Uri.parse("$CONTENT_URI_MOVIE/$added")
            }
        }

        return resultUri
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var updated: Int = 0
        when(sUriMatcher.match(uri)){
            MOVIE_ID -> updated = movieHelper.update(uri.lastPathSegment.toString(), values!!)
            TV_ID -> updated = tvHelper.update(uri.lastPathSegment.toString(), values!!)
            else -> updated = 0
        }

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}
