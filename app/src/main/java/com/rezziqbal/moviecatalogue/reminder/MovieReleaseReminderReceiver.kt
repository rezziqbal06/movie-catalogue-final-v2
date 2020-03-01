package com.rezziqbal.moviecatalogue.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.entity.Movie
import com.rezziqbal.moviecatalogue.response.MovieDataResponse
import com.rezziqbal.moviecatalogue.response.MovieResponse
import com.rezziqbal.moviecatalogue.ui.detail.DetailActivity
import com.rezziqbal.moviecatalogue.ui.detail.DetailActivity.Companion.EXTRA_MOVIE
import com.rezziqbal.moviecatalogue.utils.Api
import com.rezziqbal.moviecatalogue.utils.Cons
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MovieReleaseReminderReceiver : BroadcastReceiver() {
    companion object{
        private val TAG = MovieReleaseReminderReceiver::class.java.simpleName
        private var notificationId = 1
        private const val NOTIFICATION_REQUEST_CODE = 900

        private const val EXTRA_NOTIF_ID = "extra_notif_id"
        private const val EXTRA_EVENT = "extra_event"
        private const val CHANNEL_NAME = "movie_release_reminder"
        private const val CHANNEL_ID = "movie_release_channel_id"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        getEvents(context)
    }

    private fun getEvents(context: Context) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        Api.service.getMovieRelease(Cons.apikey, currentDate, currentDate).enqueue(object:
            retrofit2.Callback<MovieDataResponse> {
            override fun onFailure(call: Call<MovieDataResponse>, t: Throwable) {
                Log.d(TAG, "failed : "+t.message)
                Log.d(TAG, "request : "+call.request().toString())
            }

            override fun onResponse(call: Call<MovieDataResponse>, dataResponse: Response<MovieDataResponse>) {
                if(dataResponse.isSuccessful){
                    Log.d(TAG, "success : " +dataResponse.message())
                    Log.d(TAG, "request : "+call.request().toString())
                    setEventNotification(context, dataResponse.body()?.result)
                }else{
                    Log.d(TAG, "not success : " +dataResponse.message())
                }
            }

        })


    }

    private fun setEventNotification(context: Context, result: ArrayList<MovieResponse>?) {
        Thread(Runnable {
            var id: Int = notificationId
            for (mv: MovieResponse in result!!) {
                showNotification(context, mv, id)
                id++
            }
        }).start()

    }

    private fun showNotification(context: Context, mv: MovieResponse, id: Int) {
        val intent = Intent(context, DetailActivity::class.java)
        val data = Movie()

        data.id = mv.id
        data.nama = mv.nama
        data.rdate = mv.rdate
        data.vote = mv.vote
        data.deskripsi = mv.deskripsi
        data.poster = mv.poster
        data.backdrop = mv.backdrop
        data.is_favorite = "tidak"

        intent.putExtra(EXTRA_MOVIE, data)
        val pendingIntent = PendingIntent.getActivity(context,id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
            .setContentTitle(context.getString(R.string.title_release_reminder))
            .setContentText(mv.nama)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            builder.setChannelId(CHANNEL_ID)
            val notificationChannel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager?.notify(id, builder.build())

    }

    private fun getReleaseReminderPendingIntent(context: Context): PendingIntent? {
        val intent =
            Intent(context, MovieReleaseReminderReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    fun setReleaseReminder(context: Context) {
        if (getReleaseReminderPendingIntent(context) != null) {
            cancelReminder(context)
        }
        val intent =
            Intent(context, MovieReleaseReminderReceiver::class.java)
        intent.putExtra(
            EXTRA_NOTIF_ID,
            notificationId
        )
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar =
            Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 8
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        notificationId += 2
    }

    fun cancelReminder(context: Context) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager?.cancel(getReleaseReminderPendingIntent(context))
    }

}
