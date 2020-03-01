package com.rezziqbal.favoritemovieapp.ui.movie

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rezziqbal.favoritemovieapp.MainActivity
import com.rezziqbal.favoritemovieapp.R
import com.rezziqbal.favoritemovieapp.ui.detail.DetailActivity
import com.rezziqbal.favoritemovieapp.utils.Cons
import com.rezziqbal.favoritemovieapp.entity.Movie
import com.rezziqbal.favoritemovieapp.ui.detail.DetailActivity.Companion.EXTRA_MOVIE
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
                    .into(img_movie_fav)
                tv_judul_movie_fav.text = movie.nama
                tv_date_movie_fav.text = movie.rdate
                itemView.setOnClickListener{
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(EXTRA_MOVIE, movie)
                    context.startActivity(intent)
                }
                btn_delete_fav_movie.setOnClickListener{
                    onItemClickCallback?.onItemClicked(position, movie)
                }
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(position: Int, data: Movie)
    }

}