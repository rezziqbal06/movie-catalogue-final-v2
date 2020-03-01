package com.rezziqbal.moviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.rezziqbal.moviecatalogue.db.DatabaseContract
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.CONTENT_URI_TV
import com.rezziqbal.moviecatalogue.db.TvHelper

class TvProvider : ContentProvider() {

    companion object {
        private const val TV = 1
        private const val TV_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var tvHelper: TvHelper
        init {
            sUriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.TABLE_TV, TV)
            sUriMatcher.addURI(
                DatabaseContract.AUTHORITY,
                "${DatabaseContract.TABLE_TV}/#",
                TV_ID)
        }
    }
    override fun onCreate(): Boolean {
        tvHelper = TvHelper.getInstance(context as Context)
        tvHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
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
        val added: Long = when (TV) {
            sUriMatcher.match(uri) -> tvHelper.insertValue(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI_TV, null)

        return Uri.parse("${CONTENT_URI_TV}/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (TV_ID){
            sUriMatcher.match(uri) -> tvHelper.update(uri.lastPathSegment.toString(), values!!)
            else -> 0
        }
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (TV_ID) {
            sUriMatcher.match(uri) -> tvHelper.delete(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI_TV, null)

        return deleted
    }
}
