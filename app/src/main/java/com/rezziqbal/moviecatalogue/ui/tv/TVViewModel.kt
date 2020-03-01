package com.rezziqbal.moviecatalogue.ui.tv

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rezziqbal.moviecatalogue.db.MovieHelper
import com.rezziqbal.moviecatalogue.db.TvHelper
import com.rezziqbal.moviecatalogue.entity.Tv
import com.rezziqbal.moviecatalogue.response.TvDataResponse
import com.rezziqbal.moviecatalogue.response.TvResponse
import com.rezziqbal.moviecatalogue.utils.Api
import com.rezziqbal.moviecatalogue.utils.Cons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVViewModel : ViewModel() {
    private var tv = MutableLiveData<TvDataResponse>()
    private var tvData = MutableLiveData<ArrayList<Tv>>()

    fun getTv(): LiveData<TvDataResponse>{
        Api.service.getTv(Cons.apikey).enqueue(object:Callback<TvDataResponse>{
            override fun onFailure(call: Call<TvDataResponse>, t: Throwable) {
                tv.value = null
                Log.d("status1", t.message)
            }

            override fun onResponse(call: Call<TvDataResponse>, dataResponse: Response<TvDataResponse>) {
                if(dataResponse.isSuccessful){
                    Log.d("status2", dataResponse.message())
                    tv.value = dataResponse.body()
                    Log.d("isi2", dataResponse.body()?.result.toString())
                }else{
                    Log.d("status3", dataResponse.message())
                    tv.value = null
                }
            }

        })
        return tv
    }

    fun getDataFromDatabase(context: Context) : LiveData<ArrayList<Tv>>{
        val tvHelper = TvHelper(context)
        tvHelper.open()
        tvData.value = tvHelper.getAll()
        tvHelper.close()
        return tvData
    }

    fun insertDataToDatabase(context: Context, arrayTvResponse: ArrayList<TvResponse>): MutableLiveData<ArrayList<Tv>>{
        val tvHelper = TvHelper(context)
        tvHelper.open()
        try{
            tvHelper.beginTransaction()
            for(tvs in arrayTvResponse){
                tvHelper.insertTransaction(tvs)
            }
            tvHelper.setTransactionSuccessfull()
        }catch (e: Exception){
            Log.d("exception ", e.message)
        }finally {
            tvHelper.endTransaction()
        }
        tvData.value = tvHelper.getAll()
        tvHelper.close()
        return tvData
    }

    fun deleteItem(context: Context, id: String): LiveData<Int>{
        val result = MutableLiveData<Int>()
        val tvHelper = TvHelper(context)
        tvHelper.open()
        result.value = tvHelper.delete(id)
        tvHelper.close()
        return result
    }

    fun searchByApi(nama: String): LiveData<TvDataResponse>{
        Api.service.searchTvs(Cons.apikey, nama).enqueue(object: Callback<TvDataResponse>{
            override fun onFailure(call: Call<TvDataResponse>, t: Throwable) {
                Log.d("request", call.request().toString())
                tv.value = null
                Log.d("failed", t.message)
            }

            override fun onResponse(call: Call<TvDataResponse>, dataResponse: Response<TvDataResponse>) {
                Log.d("request", call.request().toString())
                if(dataResponse.isSuccessful){
                    Log.d("status2", dataResponse.message())
                    tv.value = dataResponse.body()
                }else{
                    Log.d("status3", dataResponse.message())
                    tv.value = null
                }
            }

        })
        return tv
    }
}
