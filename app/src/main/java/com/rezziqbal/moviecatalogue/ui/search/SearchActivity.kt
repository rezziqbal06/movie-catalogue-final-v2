package com.rezziqbal.moviecatalogue.ui.search

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.entity.Tv
import com.rezziqbal.moviecatalogue.response.MovieResponse
import com.rezziqbal.moviecatalogue.ui.movie.MovieAdapter
import com.rezziqbal.moviecatalogue.ui.movie.MovieFragment
import com.rezziqbal.moviecatalogue.ui.movie.MovieViewModel
import com.rezziqbal.moviecatalogue.ui.tv.TVViewModel
import com.rezziqbal.moviecatalogue.ui.tv.TvAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_movie.*
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {
    companion object{
        private var TAG = SearchActivity::class.java.simpleName
        const val TAGS = "tags"
    }

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var tvViewModel: TVViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        

        if(intent.getStringExtra(TAGS).equals("movieTag")){
            movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
            rv_search.layoutManager = GridLayoutManager(this, 2)
        }else{
            tvViewModel = ViewModelProviders.of(this).get(TVViewModel::class.java)
            rv_search.layoutManager = LinearLayoutManager(this)
        }
        
        rv_search.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.act_search).actionView as SearchView
        val tags = intent.getStringExtra(TAGS)
        searchView.isIconified = false
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        if(tags.equals("movieTag")){
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextChange(data: String?): Boolean {
                    if(data!!.length > 3){
                        Log.d(TAG, "Pencarian data: $data")
                        showProgressBar()
                        searchMovieByApi(data)
                    }
                    return true
                }

                override fun onQueryTextSubmit(data: String?): Boolean {
                    if(data!!.length > 3){
                        Log.d(TAG, "Pencarian data: $data")
                        showProgressBar()
                        searchMovieByApi(data)
                    }
                    return true
                }
            })
        }else{
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextChange(data: String?): Boolean {
                    if(data!!.length > 3){
                        Log.d(TAG, "Pencarian data: $data")
                        showProgressBar()
                        searchTvByApi(data)
                    }
                    return true
                }

                override fun onQueryTextSubmit(data: String?): Boolean {
                    if(data!!.length > 3){
                        Log.d(TAG, "Pencarian data: $data")
                        showProgressBar()
                        searchTvByApi(data)
                    }
                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun showProgressBar(){
        search_progressbar.visibility = View.VISIBLE
    }
    private fun hideProgressBar() {
        search_progressbar.visibility = View.INVISIBLE
    }

    private fun searchMovieByApi(data: String) {
        movieViewModel.searchByApi(data).observe(this, Observer {
            if(it.result != null){
                hideProgressBar()
                val dataResponse = it.result
                val movieList = ArrayList<Movie>()
                for(data in dataResponse){
                    movieList.add(
                        Movie(
                            data.id,
                            data.nama,
                            data.rdate,
                            data.vote,
                            data.deskripsi,
                            data.poster,
                            data.backdrop,
                            "tidak"
                    ))
                }
                val adapter = MovieAdapter(movieList)
                rv_search.adapter = adapter
            }else{
                hideProgressBar()
                Toast.makeText(this@SearchActivity, resources.getString(R.string.item_not_found), Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun searchTvByApi(data: String) {
        tvViewModel.searchByApi(data).observe(this, Observer {
            if(it.result != null){
                hideProgressBar()
                val dataResponse = it.result
                val tvList = ArrayList<Tv>()
                for(data in dataResponse){
                    tvList.add(Tv(
                        data.id,
                        data.nama,
                        data.rdate,
                        data.vote,
                        data.deskripsi,
                        data.poster,
                        data.backdrop,
                        "tidak"
                    ))
                }
                val adapter = TvAdapter(tvList)
                rv_search.adapter = adapter
            }else{
                hideProgressBar()
                Toast.makeText(this@SearchActivity, resources.getString(R.string.item_not_found), Toast.LENGTH_SHORT).show()
            }

        })

    }
}
