package com.rezziqbal.moviecatalogue.widget

import android.content.Intent
import android.widget.RemoteViewsService

class MovieStackService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return MovieStackRemoteViewFactory(this.applicationContext)
    }
}
