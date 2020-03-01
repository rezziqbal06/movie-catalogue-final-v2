package com.rezziqbal.moviecatalogue.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.db.MovieHelper
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.utils.Cons
import java.util.concurrent.ExecutionException

internal class MovieStackRemoteViewFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory{
    private lateinit var movieHelper: MovieHelper
    private var movies = ArrayList<Movie>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        movieHelper = MovieHelper.getInstance(context)
        movieHelper.open()
        movies = movieHelper.getByFavorite("iya")
        movieHelper.close()
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {}

    override fun getCount(): Int {
        Log.d("movie size", movies.size.toString())
        return movies.size
    }

    override fun getViewAt(position: Int): RemoteViews? {
        val remoteViews =
            RemoteViews(context.getPackageName(), R.layout.widget_item)
        if (!movies.isEmpty()) {
            val movie: Movie = movies.get(position)
            try {
                val imageBitmap: Bitmap = Glide.with(context)
                    .asBitmap()
                    .load(Cons.Companion.urlImage+"w342"+ movie.poster)
                    .submit()
                    .get()
                remoteViews.setImageViewBitmap(R.id.img_favorite_widget, imageBitmap)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val intent = Intent()
            intent.putExtra(MovieFavoriteWidget.EXTRA_ITEM, movie)
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