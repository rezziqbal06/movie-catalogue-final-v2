package com.rezziqbal.moviecatalogue.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TABLE_TV
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TBACKDROP
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TDESKRIPSI
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TNAMA
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TPOSTER
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TRDATE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.TVOTE
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.T_ID
import com.rezziqbal.moviecatalogue.db.DatabaseContract.TVColumns.Companion.T_IS_FAVORITE
import com.rezziqbal.moviecatalogue.db.DatabaseHelper
import com.rezziqbal.moviecatalogue.entity.Tv
import com.rezziqbal.moviecatalogue.response.TvResponse
import java.sql.SQLException

class TvHelper(context: Context){
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private var INSTANCE: TvHelper? = null

        fun getInstance(context: Context): TvHelper{
            if(INSTANCE == null){
                synchronized(SQLiteOpenHelper::class.java){
                    if(INSTANCE == null){
                        INSTANCE = TvHelper(context)
                    }
                }
            }
            return INSTANCE as TvHelper
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
            TABLE_TV,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun getAll(): ArrayList<Tv>{
        val cursor = database.query(
            TABLE_TV,
            null,
            null,
            null,
            null,
            null,
            "$T_ID ASC",
            null
        )
        cursor.moveToFirst()
        val arrayList = ArrayList<Tv>()
        var tv: Tv
        if(cursor.count > 0){
            do{
                tv = Tv()
                tv.id =  cursor.getLong(cursor.getColumnIndexOrThrow(T_ID))
                tv.nama = cursor.getString(cursor.getColumnIndexOrThrow(TNAMA))
                tv.deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(TDESKRIPSI))
                tv.poster = cursor.getString(cursor.getColumnIndexOrThrow(TPOSTER))
                tv.rdate = cursor.getString(cursor.getColumnIndexOrThrow(TRDATE))
                tv.vote = cursor.getDouble(cursor.getColumnIndexOrThrow(TVOTE))
                tv.backdrop = cursor.getString(cursor.getColumnIndexOrThrow(TBACKDROP))

                arrayList.add(tv)
                cursor.moveToNext()
            }while(!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insert(tv: Tv): Long{
        val initialValues = ContentValues()
        initialValues.put(T_ID, tv.id)
        initialValues.put(TNAMA, tv.nama)
        initialValues.put(TRDATE, tv.rdate)
        initialValues.put(TBACKDROP, tv.backdrop)
        initialValues.put(TPOSTER, tv.poster)
        initialValues.put(TVOTE, tv.vote)
        initialValues.put(TDESKRIPSI, tv.deskripsi)
        initialValues.put(T_IS_FAVORITE, "tidak")
        return database.insert(TABLE_TV, null, initialValues)
    }

    fun insertValue(values: ContentValues?): Long {
        return database.insert(DatabaseContract.TABLE_TV, null, values)
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

    fun insertTransaction(tvResponse: TvResponse){
        val tv: Tv = Tv()
        tv.id = tvResponse.id
        tv.nama = tvResponse.nama
        tv.rdate = tvResponse.rdate
        tv.vote = tvResponse.vote
        tv.deskripsi = tvResponse.deskripsi
        tv.poster = tvResponse.poster
        tv.backdrop = tvResponse.backdrop
        tv.is_favorite = "tidak"

        val sql = ("INSERT INTO $TABLE_TV ($T_ID, $TNAMA, $TRDATE, $TVOTE," +
                " $TDESKRIPSI," +
                " $TPOSTER," +
                " $TBACKDROP," +
                " $T_IS_FAVORITE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
        val stmt = database.compileStatement(sql)
        tv.id?.let { stmt.bindLong(1, it) }
        stmt.bindString(2, tv.nama)
        stmt.bindString(3, tv.rdate)
        stmt.bindDouble(4, tv.vote!!)
        stmt.bindString(5, tv.deskripsi)
        stmt.bindString(6, tv.poster)
        stmt.bindString(7, tv.backdrop)
        stmt.bindString(8, tv.is_favorite)
        stmt.execute()
        stmt.clearBindings()
    }


    fun getById(id: String): Cursor {
        return database.query(
            TABLE_TV,
            null,
            "$T_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun update(id: String, values: ContentValues): Int{
        return database.update(TABLE_TV, values, "$T_ID = ?", arrayOf(id))
    }

    fun delete(id: String): Int{
        return database.delete(TABLE_TV, "$T_ID = ?", arrayOf(id))
    }

    fun getByFavorite(isFavorite: String): ArrayList<Tv> {
        val cursor = database.query(
            TABLE_TV,
            null,
            "$T_IS_FAVORITE = ?",
            arrayOf(isFavorite),
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val arrayList = ArrayList<Tv>()
        var tv: Tv
        if(cursor.count > 0){
            do{
                tv = Tv()
                tv.id =  cursor.getLong(cursor.getColumnIndexOrThrow(T_ID))
                tv.nama = cursor.getString(cursor.getColumnIndexOrThrow(TNAMA))
                tv.deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(TDESKRIPSI))
                tv.poster = cursor.getString(cursor.getColumnIndexOrThrow(TPOSTER))
                tv.rdate = cursor.getString(cursor.getColumnIndexOrThrow(TRDATE))
                tv.vote = cursor.getDouble(cursor.getColumnIndexOrThrow(TVOTE))
                tv.backdrop = cursor.getString(cursor.getColumnIndexOrThrow(TBACKDROP))
                tv.is_favorite = cursor.getString(cursor.getColumnIndexOrThrow(T_IS_FAVORITE))

                arrayList.add(tv)
                cursor.moveToNext()
            }while(!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun queryByFavorite(isFavorite: String): Cursor {
        return database.query(
            TABLE_TV,
            null,
            "$T_IS_FAVORITE = ?",
            arrayOf(isFavorite),
            null,
            null,
            null,
            null
        )
    }

}