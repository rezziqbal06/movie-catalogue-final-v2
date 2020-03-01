package com.rezziqbal.moviecatalogue.ui.tv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.db.TvHelper
import com.rezziqbal.moviecatalogue.entity.Tv
import com.rezziqbal.moviecatalogue.response.TvResponse
import com.rezziqbal.moviecatalogue.ui.search.SearchActivity
import com.rezziqbal.moviecatalogue.ui.search.SearchActivity.Companion.TAGS
import kotlinx.android.synthetic.main.fragment_tv.*

class TVFragment : Fragment() {

    companion object{
        const val STATE_RESULT = "state_result"
        private val TAG = TVFragment::class.java.simpleName
    }
    private var arrayTvResponse: ArrayList<TvResponse>? = ArrayList<TvResponse>()
    private var tv: ArrayList<Tv> = ArrayList<Tv>()

    private lateinit var tvViewModel: TVViewModel
    private lateinit var tvHelper: TvHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tvViewModel = ViewModelProviders.of(this).get(TVViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tv, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvHelper = TvHelper.getInstance(activity!!.applicationContext)


        tvViewModel.getDataFromDatabase(activity!!).observe(this, Observer {
            tv = it
            if(tv.size == 0){
                showLoader()
                tvViewModel.getTv().observe(this, Observer {
                    Log.d(TAG, "ambil data dari api")
                    hideLoader()
                    arrayTvResponse = it?.result
                    setData(arrayTvResponse)
                })
            }else{
                Log.d(TAG, "gausah ambil data")
                setDataToAdapter(tv)
            }
        })
        btn_search_tv.setOnClickListener{
            val intent = Intent(activity!!, SearchActivity::class.java)
            intent.putExtra(TAGS, "tvTag")
            startActivity(intent)
        }


        if(savedInstanceState != null){
            val arrayTvList: ArrayList<Tv>? = savedInstanceState.getParcelableArrayList(STATE_RESULT)
            arrayTvList?.let { setDataToAdapter(it) }
        }
    }

    private fun setData(arrayTvList: ArrayList<TvResponse>?) {
        Log.d(TAG, "insert data")
        arrayTvList?.let { tvViewModel.insertDataToDatabase(activity!!.applicationContext, it).observe(this, Observer {
            tv = it
            setDataToAdapter(tv)
        })
        }
    }

    private fun setDataToAdapter(tv: ArrayList<Tv>?) {
        Log.d(TAG, "set to adapter")
        rv_tv.layoutManager = LinearLayoutManager(activity)
        rv_tv.setHasFixedSize(true)
        val adapter = tv?.let { TvAdapter(it) }
        rv_tv.adapter = adapter
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_RESULT, tv)
    }

    private fun showLoader() {
        tv_progressbar.visibility = View.VISIBLE
    }

    private fun hideLoader(){
        tv_progressbar.visibility = View.INVISIBLE
    }

}