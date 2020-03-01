package com.rezziqbal.favoritemovieapp.ui.tv

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rezziqbal.favoritemovieapp.R
import com.rezziqbal.favoritemovieapp.entity.Tv
import com.rezziqbal.favoritemovieapp.ui.detail.DetailActivity
import com.rezziqbal.favoritemovieapp.utils.Cons
import kotlinx.android.synthetic.main.tv_item.view.*

class TvAdapter(private val listTv: ArrayList<Tv>) : RecyclerView.Adapter<TvAdapter.ViewHolder>(){
    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listTv.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, listTv[position])
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
                }
                btn_delete_fav_tv.setOnClickListener { onItemClickCallback?.onItemClicked(position, tv) }
            }
           
        }
    }
    
    interface OnItemClickCallback{
        fun onItemClicked(position: Int, data: Tv)
    }

}