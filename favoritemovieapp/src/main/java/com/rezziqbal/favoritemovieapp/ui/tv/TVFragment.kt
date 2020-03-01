package com.rezziqbal.favoritemovieapp.ui.tv

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rezziqbal.favoritemovieapp.R
import com.rezziqbal.favoritemovieapp.entity.Tv
import com.rezziqbal.favoritemovieapp.MainViewModel
import kotlinx.android.synthetic.main.fragment_tv.*

class TVFragment : Fragment() {

    companion object{
        const val STATE_RESULT = "state_result"
        private val TAG = TVFragment::class.java.simpleName
    }
    private var tv: ArrayList<Tv> = ArrayList<Tv>()

    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tv, container, false)
        mViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoader()
        loadTv()
        if(savedInstanceState != null){
            val arrayTvList: ArrayList<Tv>? = savedInstanceState.getParcelableArrayList(STATE_RESULT)
            arrayTvList?.let { setDataToAdapter(it) }
        }
    }

    private fun loadTv() {
        mViewModel.getTv().observe(requireActivity(), Observer {
            hideLoader()
            if(it != null){
                tv = it
                setDataToAdapter(tv)
            }else{
                showMessage(resources.getString(R.string.not_found))
            }
        })
    }

    private fun showMessage(string: String) {
        Toast.makeText(activity!!, string, Toast.LENGTH_SHORT).show()
    }

    private fun setDataToAdapter(tv: ArrayList<Tv>?) {
        Log.d(TAG, "set to adapter")
        rv_tv.layoutManager = LinearLayoutManager(activity)
        rv_tv.setHasFixedSize(true)
        val adapter = tv?.let { TvAdapter(it) }
        rv_tv.adapter = adapter
        adapter?.setOnItemClickCallback(object: TvAdapter.OnItemClickCallback{
            override fun onItemClicked(position: Int, data: Tv) {
                mViewModel.removeFavoriteTv(activity!!, data.id.toString()).observe(activity!!, Observer {
                    if(it > 0){
                        adapter.removeItem(position)
                    }
                })
            }

        })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_RESULT, tv)
    }

    private fun showLoader() {
        tv_progressbar.visibility = View.VISIBLE
    }

    private fun hideLoader(){
        tv_progressbar.visibility = View.GONE
    }

}