package com.rezziqbal.moviecatalogue.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.MBACKDROP
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.MDESKRIPSI
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.MNAMA
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.MPOSTER
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.MRDATE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.MVOTE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.M_ID
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.M_IS_FAVORITE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TBACKDROP
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TDESKRIPSI
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TNAMA
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TPOSTER
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TRDATE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TVOTE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TABLE_MOVIE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TABLE_TV
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.T_ID
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.T_IS_FAVORITE

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "dbcinema"
        private const val DATABASE_VERSION = 6

        private val CREATE_TABLE_MOVIE = "create table $TABLE_MOVIE (" +
                "$M_ID integer primary key autoincrement," +
                " $MNAMA text not null," +
                " $MRDATE text not null," +
                " $MVOTE not null," +
                " $MDESKRIPSI text not null," +
                " $MPOSTER text," +
                " $MBACKDROP text," +
                " $M_IS_FAVORITE text not null);"

        private val CREATE_TABLE_TV = "create table $TABLE_TV (" +
                "$T_ID integer primary key autoincrement," +
                " $TNAMA text not null," +
                " $TRDATE text not null," +
                " $TVOTE not null," +
                " $TDESKRIPSI text not null," +
                " $TPOSTER text," +
                " $TBACKDROP text," +
                " $T_IS_FAVORITE text not null);"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_MOVIE)
        db?.execSQL(CREATE_TABLE_TV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TV")
        onCreate(db)
    }


}