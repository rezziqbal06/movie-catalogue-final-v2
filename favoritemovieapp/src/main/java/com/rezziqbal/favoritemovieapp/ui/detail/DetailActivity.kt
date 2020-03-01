package com.rezziqbal.favoritemovieapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.rezziqbal.favoritemovieapp.R
import com.rezziqbal.favoritemovieapp.utils.Cons
import com.rezziqbal.favoritemovieapp.entity.Movie
import com.rezziqbal.favoritemovieapp.entity.Tv
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


        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
