package com.rezziqbal.favoritemovieapp.ui.tv

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rezziqbal.favoritemovieapp.db.DatabaseContract
import com.rezziqbal.favoritemovieapp.db.DatabaseContract.TVColumns.Companion.CONTENT_URI_TV
import com.rezziqbal.favoritemovieapp.entity.Tv
import com.rezziqbal.favoritemovieapp.utils.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TVViewModel : ViewModel() {
    private var tvData = MutableLiveData<ArrayList<Tv>>()

    fun getDataFromDatabase(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredTv = async(Dispatchers.IO) {
                val cursor = context.contentResolver?.query(CONTENT_URI_TV, null, null, null, null) as Cursor
                MappingHelper.mapTvCursorToArray(cursor)
            }
            val tv = deferredTv.await()
            if (tv != null) {
                tvData.value = tv
            } else {
                tvData.value = null
            }
        }
    }

    fun getTv(): LiveData<ArrayList<Tv>>{
        return tvData
    }

    fun removeFavoriteTv(context: Context, idTv: String): LiveData<Int>{
        val uriWithId = Uri.parse(DatabaseContract.TVColumns.CONTENT_URI_TV.toString() + "/" + idTv)

        val result = MutableLiveData<Int>()
        val values = ContentValues()
        values.put(DatabaseContract.TVColumns.T_IS_FAVORITE, "tidak")
        result.value = context.contentResolver.update(uriWithId, values, null, null)
        return result
    }
}
