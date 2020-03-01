package com.rezziqbal.moviecatalogue.ui.detail

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.entity.Tv
import com.rezziqbal.moviecatalogue.ui.favorite.FavoriteActivity
import com.rezziqbal.moviecatalogue.utils.Cons
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_TV = "extra_tv"
    }

    private lateinit var viewModel: DetailViewModel
    private var dataMovie: Movie? = Movie()
    private var dataTv: Tv? = Tv()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)



        dataMovie = intent.getParcelableExtra(EXTRA_MOVIE)
        dataTv = intent.getParcelableExtra(EXTRA_TV)

        dataMovie?.let { it-> setDataMovie(it)}

        dataTv?.let { it-> setDataTv(it)}

    }

    private fun setDataTv(tv: Tv) {
        Log.d("action ","setDataTv")
        Log.d("tv ",tv.toString())
        viewModel.getDetailTv(this, tv).observe(this, Observer {
            val data = it
            progressBar.visibility = View.GONE
            if(data.backdrop != null){
                Glide.with(this)
                    .load(Cons.urlImage+"w185"+data?.backdrop)
                    .error(resources.getDrawable(R.drawable.ic_broken_image))
                    .into(img_backdrop)
            }

            if(data.poster != null){
                Glide.with(this)
                    .load(Cons.urlImage+"w185"+data?.poster)
                    .error(resources.getDrawable(R.drawable.ic_broken_image))
                    .into(img_poster)
            }


            tv_nama.text = data?.nama
            tv_deskripsi.text = data?.deskripsi
            tv_date.text = data?.rdate
            tv_vote.text = data?.vote.toString()
            supportActionBar?.title = resources.getString(R.string.detail_tv)

            if(data.is_favorite.equals("iya")){
                setSavedFav()
            }else{
                setUnSaveFav()
            }

            btn_favorite.setOnClickListener{
                data.is_favorite?.let { it1 -> setFavoriteTv(it1, tv.id.toString()) }
            }
        })

    }



    private fun setDataMovie(movie: Movie) {
        viewModel.getDetailMovie(this, movie).observe(this, Observer {
            val data = it
            progressBar.visibility = View.GONE
            if(data.backdrop != null){
                Glide.with(this)
                    .load(Cons.urlImage+"w185"+data?.backdrop)
                    .error(resources.getDrawable(R.drawable.ic_broken_image))
                    .into(img_backdrop)
            }

            if(data.poster != null){
                Glide.with(this)
                    .load(Cons.urlImage+"w185"+data?.poster)
                    .error(resources.getDrawable(R.drawable.ic_broken_image))
                    .into(img_poster)
            }
            tv_nama.text = data?.nama
            tv_deskripsi.text = data?.deskripsi
            tv_date.text = data?.rdate
            tv_vote.text = data?.vote.toString()
            supportActionBar?.title = resources.getString(R.string.detail_movie)

            if(data.is_favorite.equals("iya")){
                btn_favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark_black_24dp))
            }else{
                btn_favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark))
            }

            btn_favorite.setOnClickListener{
                data.is_favorite?.let { it1 -> setFavoriteMovie(it1, movie.id.toString()) }
            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
            R.id.fav_movie -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                intent.putExtra(FavoriteActivity.KEY_FAV, "key_movie")
                startActivity(intent)
            }
            R.id.fav_tv -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                intent.putExtra(FavoriteActivity.KEY_FAV, "key_tv")
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setFavoriteTv(is_favorite: String,idTv: String) {
        if(is_favorite.equals("tidak")){
            addFavoriteTv(idTv)
        }else{
            removeFavoriteTv(idTv)
        }
    }



    private fun setFavoriteMovie(is_favorite: String, idMovie: String) {
        if(is_favorite.equals("tidak")){
            addFavoriteMovie(idMovie)
        }else{
            removeFavoriteMovie(idMovie)
        }
    }

    private fun removeFavoriteMovie(idMovie: String) {
        viewModel.removeFavoriteMovie(this, idMovie).observe(this, Observer {
            if(it > 0){
                setUnSaveFav()
            }
        })
    }

    private fun addFavoriteMovie(id: String) {
        viewModel.addFavoriteMovie(this, id).observe(this, Observer {
            if( it > 0){
                setSavedFav()
            }
        })
    }

    private fun removeFavoriteTv(idTv: String) {
        viewModel.removeFavoriteTv(this, idTv).observe(this, Observer {
            if( it > 0){
                setUnSaveFav()
            }
        })
    }

    private fun addFavoriteTv(id: String) {
        viewModel.addFavoriteTv(this, id).observe(this, Observer {
            if( it > 0){
                setSavedFav()
            }
        })
    }

    private fun setSavedFav() {
        btn_favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark_black_24dp))
    }

    private fun setUnSaveFav() {
        btn_favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark))
    }
}
