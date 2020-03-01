package com.rezziqbal.moviecatalogue.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.db.TvHelper
import com.rezziqbal.moviecatalogue.entity.Tv
import com.rezziqbal.moviecatalogue.utils.Cons
import java.util.concurrent.ExecutionException

internal class TvStackRemoteViewFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory{
    private lateinit var tvHelper: TvHelper
    private var tvs = ArrayList<Tv>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        tvHelper = TvHelper.getInstance(context)
        tvHelper.open()
        tvs = tvHelper.getByFavorite("iya")
        tvHelper.close()
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {}

    override fun getCount(): Int {
        return tvs.size

    }

    override fun getViewAt(position: Int): RemoteViews? {
        val remoteViews =
            RemoteViews(context.getPackageName(), R.layout.widget_item)
        if(!tvs.isEmpty()){
            val tv: Tv = tvs.get(position)
            try {
                val imageBitmap: Bitmap = Glide.with(context)
                    .asBitmap()
                    .load(Cons.Companion.urlImage+"w342"+ tv.poster)
                    .submit()
                    .get()
                remoteViews.setImageViewBitmap(R.id.img_favorite_widget, imageBitmap)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val intent = Intent()
            intent.putExtra(TvFavoriteWidget.EXTRA_ITEM, tv)
            remoteViews.setOnClickFillInIntent(R.id.img_favorite_widget, intent)
        }
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

}