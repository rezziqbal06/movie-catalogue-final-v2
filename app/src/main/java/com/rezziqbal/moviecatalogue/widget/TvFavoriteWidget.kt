package com.rezziqbal.moviecatalogue.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.entity.Tv
import com.rezziqbal.moviecatalogue.ui.detail.DetailActivity

/**
 * Implementation of App Widget functionality.
 */
class TvFavoriteWidget : AppWidgetProvider() {
    companion object {
        private const val KEY_ACTION = "com.rezziqbal.moviecatalogue.KEY_ACTION"
        const val EXTRA_ITEM = "com.rezziqbal.moviecatalogue.EXTRA_ITEM"

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, TvStackService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.tv_favorite_widget)
            views.setRemoteAdapter(R.id.stack_view_tv, intent)
            views.setEmptyView(R.id.stack_view_tv, R.id.empty_view_tv)

            val actionIntent = Intent(context, TvFavoriteWidget::class.java)
            actionIntent.action = KEY_ACTION
            actionIntent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val pendingIntent = PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view_tv, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null && intent.action == KEY_ACTION) {
            val tv: Tv? = intent.getParcelableExtra(EXTRA_ITEM)
            tv?.let { openMovieDetail(context, it) }
        } else {
            Log.d("test", "test")
        }
        updateWidgetComponent(context)
    }

    private fun updateWidgetComponent(context: Context) {
        val widgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, TvFavoriteWidget::class.java)
        val widgetIds = widgetManager.getAppWidgetIds(componentName)
        if (widgetIds != null) {
            widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.stack_view_tv)
        }

    }

    private fun openMovieDetail(context: Context, tv: Tv) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_TV, tv)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
