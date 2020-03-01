package com.rezziqbal.moviecatalogue.ui.favorite

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.db.DatabaseContract
import com.rezziqbal.moviecatalogue.db.TvHelper
import com.rezziqbal.moviecatalogue.entity.Tv
import com.rezziqbal.moviecatalogue.ui.detail.DetailActivity
import com.rezziqbal.moviecatalogue.utils.Cons
import kotlinx.android.synthetic.main.tv_item_favorite.view.*

class TvFavoriteAdapter(private val listTv: ArrayList<Tv>) : RecyclerView.Adapter<TvFavoriteAdapter.ViewHolder>(){
    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tv_item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listTv.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tv = listTv[position]
        val context = holder.itemView.context
        holder.bind(position, listTv[position])

    }

    private fun setFavorite(context: Context, id: String, isFavorite: String) {
        val tvHelper = TvHelper(context)
        val values = ContentValues()
        values.put(DatabaseContract.TVColumns.T_IS_FAVORITE, isFavorite)
        tvHelper.open()
        tvHelper.update(id, values)
        tvHelper.close()
    }

    fun removeItem(position: Int){
        this.listTv.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, this.listTv.size)
    }

    fun updateItem(position: Int, tv: Tv) {
        this.listTv[position] = tv
        notifyItemChanged(position, tv)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, tv: Tv) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(Cons.Companion.urlImage + "w185" + tv.poster)
                    .into(img_tv_fav)
                tv_judul_tv_fav.text = tv.nama
                tv_date_tv_fav.text = tv.rdate
                itemView.setOnClickListener{
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_TV, tv)
                    context.startActivity(intent)
                    (context as FavoriteActivity).finish()
                }
                btn_delete_fav_tv.setOnClickListener { onItemClickCallback?.onItemClicked(position, tv) }
            }
           
        }
    }
    
    interface OnItemClickCallback{
        fun onItemClicked(position: Int, data: Tv)
    }

}