package com.rezziqbal.favoritemovieapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract{
    var TABLE_MOVIE = "movie"
    var TABLE_TV = "tv"

    const val AUTHORITY = "com.rezziqbal.moviecatalogue.provider"
    const val SCHEME = "content"

    internal class MovieColumns : BaseColumns{
        companion object{
            const val M_ID = "_id"
            const val MNAMA = "nama"
            const val MRDATE = "rdate"
            const val MVOTE = "vote"
            const val MDESKRIPSI = "deskripsi"
            const val MPOSTER = "poster"
            const val MBACKDROP = "backdrop"
            const val M_IS_FAVORITE = "is_favorite"

            val CONTENT_URI_MOVIE: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build()
        }
    }

    internal class TVColumns : BaseColumns{
        companion object{
            const val T_ID = "_id"
            const val TNAMA = "nama"
            const val TRDATE = "rdate"
            const val TVOTE = "vote"
            const val TDESKRIPSI = "deskripsi"
            const val TPOSTER = "poster"
            const val TBACKDROP = "backdrop"
            const val T_IS_FAVORITE = "is_favorite"

            val CONTENT_URI_TV: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_TV)
                .build()
        }
    }
}
