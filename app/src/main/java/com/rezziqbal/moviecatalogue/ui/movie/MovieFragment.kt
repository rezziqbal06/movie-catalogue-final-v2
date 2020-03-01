package com.rezziqbal.moviecatalogue.ui.movie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.db.MovieHelper
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.response.MovieResponse
import com.rezziqbal.moviecatalogue.ui.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {

    companion object{
        const val STATE_RESULT = "state_result"
        private val TAG = MovieFragment::class.java.simpleName
    }
    private var arrayMovieResponse: ArrayList<MovieResponse>? = ArrayList<MovieResponse>()
    private var movie: ArrayList<Movie> = ArrayList<Movie>()

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieHelper: MovieHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie, container, false)
        movieHelper = MovieHelper.getInstance(activity!!)

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        movieViewModel.getDataFromDatabase(activity!!).observe(activity!!, Observer {
            movie = it
            if(movie.size == 0){
                Log.d(TAG, "ambil data")
                showLoader()
                movieViewModel.getMovie().observe(this, Observer {
                    hideLoader()
                    arrayMovieResponse = it?.result

                    setData(arrayMovieResponse)
                })
            }else{
                Log.d(TAG, "gausah ambil data")
                setDataToAdapter(movie)
            }
        })

        btn_search.setOnClickListener{
            val intent = Intent(activity!!, SearchActivity::class.java)
            intent.putExtra(SearchActivity.TAGS, "movieTag")
            startActivity(intent)
        }

        if(savedInstanceState != null){
            val arrayMovieList: ArrayList<Movie>? = savedInstanceState.getParcelableArrayList(STATE_RESULT)
            arrayMovieList?.let { setDataToAdapter(it) }
        }
    }

    private fun setData(arrayMovieList: ArrayList<MovieResponse>?) {
        arrayMovieList?.let { movieViewModel.insertDataToDatabase(activity!!.applicationContext, it).observe(this, Observer {
            movie = it
            setDataToAdapter(movie)
        })
        }
    }

    private fun setDataToAdapter(movie: ArrayList<Movie>) {
        rv_movie.layoutManager = GridLayoutManager(activity, 2)
        rv_movie.setHasFixedSize(true)
        val adapter = MovieAdapter(movie)
        rv_movie.adapter = adapter
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_RESULT, movie)
    }

    private fun showLoader() {
        movie_progressbar.visibility = View.VISIBLE
    }

    private fun hideLoader(){
        movie_progressbar.visibility = View.INVISIBLE
    }

}