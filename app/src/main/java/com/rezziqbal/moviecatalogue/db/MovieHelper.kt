package com.rezziqbal.moviecatalogue.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TABLE_MOVIE
import com.rezziqbal.moviecatalogue.entity.Movie
import java.sql.SQLException

class MovieHelper(context: Context){
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private var INSTANCE: MovieHelper? = null

        fun getInstance(context: Context): MovieHelper{
            if(INSTANCE == null){
                synchronized(SQLiteOpenHelper::class.java){
                    if(INSTANCE == null){
                        INSTANCE = MovieHelper(context)
                    }
                }
            }
            return INSTANCE as MovieHelper
        }
    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()
        if(database.isOpen){
            database.close()
        }
    }

    fun queryAll(): Cursor {
        return database.query(
            TABLE_MOVIE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun getAll(): ArrayList<Movie>{
        val cursor = database.query(
            TABLE_MOVIE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null
        )
        cursor.moveToFirst()
        val arrayList = ArrayList<Movie>()
        var movie: Movie
        if(cursor.count > 0){
            do{
                movie = Movie()
                movie.id =  cursor.getLong(cursor.getColumnIndexOrThrow(M_ID))
                movie.nama = cursor.getString(cursor.getColumnIndexOrThrow(MNAMA))
                movie.deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(MDESKRIPSI))
                movie.poster = cursor.getString(cursor.getColumnIndexOrThrow(MPOSTER))
                movie.rdate = cursor.getString(cursor.getColumnIndexOrThrow(MRDATE))
                movie.vote = cursor.getDouble(cursor.getColumnIndexOrThrow(MVOTE))
                movie.backdrop = cursor.getString(cursor.getColumnIndexOrThrow(MBACKDROP))

                arrayList.add(movie)
                cursor.moveToNext()
            }while(!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insert(movie: Movie): Long{
        val initialValues = ContentValues()
        initialValues.put(M_ID, movie.id)
        initialValues.put(MNAMA, movie.nama)
        initialValues.put(MRDATE, movie.rdate)
        initialValues.put(MBACKDROP, movie.backdrop)
        initialValues.put(MPOSTER, movie.poster)
        initialValues.put(MVOTE, movie.vote)
        initialValues.put(MDESKRIPSI, movie.deskripsi)
        initialValues.put(M_IS_FAVORITE, "tidak")
        return database.insert(TABLE_MOVIE, null, initialValues)
    }

    fun insertValue(values: ContentValues?): Long {
        return database.insert(TABLE_MOVIE, null, values)
    }

    fun beginTransaction(){
        database.beginTransaction()
    }

    fun setTransactionSuccessfull(){
        database.setTransactionSuccessful()
    }

    fun endTransaction(){
        database.endTransaction()
    }

    fun insertTransaction(movieResponse: com.rezziqbal.moviecatalogue.response.MovieResponse){
        val movie: Movie = Movie()
        movie.id = movieResponse.id
        movie.nama = movieResponse.nama
        movie.rdate = movieResponse.rdate
        movie.vote = movieResponse.vote
        movie.deskripsi = movieResponse.deskripsi
        movie.poster = movieResponse.poster
        movie.backdrop = movieResponse.backdrop
        movie.is_favorite = "tidak"

        val sql = ("INSERT INTO $TABLE_MOVIE ($M_ID, $MNAMA, $MRDATE, $MVOTE," +
                " $MDESKRIPSI," +
                " $MPOSTER," +
                " $MBACKDROP," +
                " $M_IS_FAVORITE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
        val stmt = database.compileStatement(sql)
        stmt.bindLong(1, movie.id!!)
        stmt.bindString(2, movie.nama)
        stmt.bindString(3, movie.rdate)
        stmt.bindDouble(4, movie.vote!!)
        stmt.bindString(5, movie.deskripsi)
        stmt.bindString(6, movie.poster)
        stmt.bindString(7, movie.backdrop)
        stmt.bindString(8, movie.is_favorite)
        stmt.execute()
        stmt.clearBindings()
    }


    fun getById(id: String): Cursor {
        return database.query(
            TABLE_MOVIE,
            null,
            "$M_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun update(id: String, values: ContentValues): Int{
        return database.update(TABLE_MOVIE, values, "$M_ID = ?", arrayOf(id))
    }

    fun delete(id: String): Int{
        return database.delete(TABLE_MOVIE, "$M_ID = ?", arrayOf(id))
    }

    fun queryByFavorite(isFavorite: String): Cursor{
        val cursor = database.query(
            TABLE_MOVIE,
            null,
            "$M_IS_FAVORITE = ?",
            arrayOf(isFavorite),
            null,
            null,
            null,
            null
        )
        return cursor
    }

    fun getByFavorite(isFavorite: String): ArrayList<Movie> {
        val cursor = database.query(
            TABLE_MOVIE,
            null,
            "$M_IS_FAVORITE = ?",
            arrayOf(isFavorite),
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val arrayList = ArrayList<Movie>()
        var movie: Movie
        if(cursor.count > 0){
            do{
                movie = Movie()
                movie.id =  cursor.getLong(cursor.getColumnIndexOrThrow(M_ID))
                movie.nama = cursor.getString(cursor.getColumnIndexOrThrow(MNAMA))
                movie.deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(MDESKRIPSI))
                movie.poster = cursor.getString(cursor.getColumnIndexOrThrow(MPOSTER))
                movie.rdate = cursor.getString(cursor.getColumnIndexOrThrow(MRDATE))
                movie.vote = cursor.getDouble(cursor.getColumnIndexOrThrow(MVOTE))
                movie.backdrop = cursor.getString(cursor.getColumnIndexOrThrow(MBACKDROP))
                movie.is_favorite = cursor.getString(cursor.getColumnIndexOrThrow(M_IS_FAVORITE))

                arrayList.add(movie)
                cursor.moveToNext()
            }while(!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

}