package com.rezziqbal.moviecatalogue.ui.movie

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.db.DatabaseContract.MovieColumns.Companion.M_IS_FAVORITE
import com.rezziqbal.moviecatalogue.db.MovieHelper
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.ui.detail.DetailActivity
import com.rezziqbal.moviecatalogue.ui.detail.DetailActivity.Companion.EXTRA_MOVIE
import com.rezziqbal.moviecatalogue.utils.Cons
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter(private val listMovie: ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>(){

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listMovie.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = listMovie[position]
        val context = holder.itemView.context
        holder.bind(position,listMovie[position])

    }

    fun removeItem(position: Int){
        this.listMovie.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, this.listMovie.size)
    }

    fun updateItem(position: Int, movie: Movie) {
        this.listMovie[position] = movie
        notifyItemChanged(position, movie)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, movie:Movie){
            with(itemView){
                Glide.with(context)
                    .load(Cons.Companion.urlImage+"w185"+movie.poster)
                    .into(img_movie)
                tv_judul_movie.text = movie.nama
                tv_date_movie.text = movie.rdate
                itemView.setOnClickListener{
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(EXTRA_MOVIE, movie)
                    context.startActivity(intent)
                }
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(position: Int, data: Movie)
    }

}