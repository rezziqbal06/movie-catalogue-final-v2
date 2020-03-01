package com.rezziqbal.moviecatalogue.widget

import android.content.Intent
import android.widget.RemoteViewsService

class TvStackService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return TvStackRemoteViewFactory(this.applicationContext)
    }
}
