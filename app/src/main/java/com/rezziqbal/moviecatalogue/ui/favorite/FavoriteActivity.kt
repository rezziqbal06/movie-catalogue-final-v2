package com.rezziqbal.moviecatalogue.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.entity.Tv
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {
    companion object{
        val KEY_FAV: String? = null
        private val TAG = FavoriteActivity::class.java.simpleName
    }
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)


        val key_fav = intent.getStringExtra(KEY_FAV)


        if(key_fav.equals("key_movie")){
            setDataMovieAdapter()
            supportActionBar?.title = resources.getString(R.string.title_favorite_movie)
        }else{
            setDataTvAdapter()
            supportActionBar?.title = resources.getString(R.string.title_favorite_tv)
        }

    }

    fun setDataMovieAdapter(){
        Log.d(TAG, "movie favorite")
        rv_favorite.layoutManager = GridLayoutManager(this, 2)
        rv_favorite.setHasFixedSize(true)
        viewModel.getMovieByFavorite(this).observe(this, Observer {
            Log.d("dataMovie", it.size.toString())
            val adapter = MovieFavoriteAdapter(it)
            rv_favorite.adapter = adapter
            adapter.setOnItemClickCallback(object: MovieFavoriteAdapter.OnItemClickCallback{
                override fun onItemClicked(position: Int, data: Movie) {
                    viewModel.removeFavoriteMovie(this@FavoriteActivity, data.id.toString()).observe(this@FavoriteActivity, Observer {
                        if(it > 0){
                            adapter.removeItem(position)
                        }
                    })
                }
            })
        })
    }

    fun setDataTvAdapter(){
        Log.d(TAG, "tv favorite")
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        viewModel.getTvByFavorite(this).observe(this, Observer {
            val adapter = TvFavoriteAdapter(it)
            rv_favorite.adapter = adapter
            adapter.setOnItemClickCallback(object: TvFavoriteAdapter.OnItemClickCallback{
                override fun onItemClicked(position: Int, data: Tv) {
                    viewModel.removeFavoriteTv(this@FavoriteActivity, data.id.toString()).observe(this@FavoriteActivity, Observer {
                        if(it > 0){
                            adapter.removeItem(position)
                        }
                    })
                }
            })
        })
    }
}
