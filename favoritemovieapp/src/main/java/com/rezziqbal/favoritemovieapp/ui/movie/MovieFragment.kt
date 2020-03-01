package com.rezziqbal.favoritemovieapp.ui.movie

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rezziqbal.favoritemovieapp.R
import com.rezziqbal.favoritemovieapp.db.DatabaseContract
import com.rezziqbal.favoritemovieapp.db.DatabaseContract.MovieColumns.Companion.CONTENT_URI_MOVIE
import com.rezziqbal.favoritemovieapp.entity.Movie
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {

    companion object{
        const val STATE_RESULT = "state_result"
    }
    private var movie: ArrayList<Movie> = ArrayList<Movie>()

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie, container, false)

        movieViewModel = ViewModelProvider(requireActivity())[MovieViewModel::class.java]
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoader()
        loadMovie()
        if(savedInstanceState != null){
            val arrayMovieList: ArrayList<Movie>? = savedInstanceState.getParcelableArrayList(STATE_RESULT)
            arrayMovieList?.let { setDataToAdapter(it) }
        }
    }

    private fun loadMovie() {
        movieViewModel.getMovie().observe(requireActivity(), Observer {
            hideLoader()
            if(it != null){
                setDataToAdapter(it)
                movie = it
                Log.d("movie ", it.toString())
            }else{
                showMessage(resources.getString(R.string.not_found))
            }
        })
    }

    private fun setDataToAdapter(movie: ArrayList<Movie>) {
        rv_movie.layoutManager = GridLayoutManager(activity!!, 2)
        rv_movie.setHasFixedSize(true)
        val adapter = MovieAdapter(movie)
        rv_movie.adapter = adapter
        adapter.setOnItemClickCallback(object: MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(position: Int, data: Movie) {
                movieViewModel.removeFavoriteMovie(activity!!, data.id.toString()).observe(activity!!, Observer {
                    if(it > 0){
                        adapter.removeItem(position)
                    }else{
                        showMessage(resources.getString(R.string.remove_failed))
                    }
                })
            }

        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(activity!!, message, Toast.LENGTH_SHORT).show()
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
