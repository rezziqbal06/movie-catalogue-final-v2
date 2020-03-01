package com.rezziqbal.moviecatalogue.ui.settingreminder

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.rezziqbal.moviecatalogue.R
import com.rezziqbal.moviecatalogue.reminder.DailyReminderReceiver
import com.rezziqbal.moviecatalogue.reminder.MovieReleaseReminderReceiver

class ReminderMovieFragment : PreferenceFragmentCompat(),
    androidx.preference.Preference.OnPreferenceChangeListener {
    companion object{
        private const val KEY_DAILY_REMINDER = "key_daily_reminder"
        private const val KEY_RELEASE_REMINDER = "key_release_reminder"
        private var dailyReminderReceiver: DailyReminderReceiver? = null
        private var releaseReminderReceiver: MovieReleaseReminderReceiver? = null
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.reminder_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dailyReminderReceiver = DailyReminderReceiver()
        releaseReminderReceiver = MovieReleaseReminderReceiver()

        val switchDailyReminder = findPreference<SwitchPreferenceCompat>(KEY_DAILY_REMINDER)
        val switchReleaseReminder = findPreference<SwitchPreferenceCompat>(KEY_RELEASE_REMINDER)

        if (switchDailyReminder != null && switchReleaseReminder != null) {
            switchDailyReminder.setOnPreferenceChangeListener(this)
            switchReleaseReminder.setOnPreferenceChangeListener(this)
        }

    }

    override fun onPreferenceChange(
        preference: androidx.preference.Preference?,
        newValue: Any?
    ): Boolean {
        val prefKey = preference!!.key
        val isActive = newValue as Boolean
        if(prefKey.equals(KEY_DAILY_REMINDER) && context != null){
            if(isActive){
                showMessage(resources.getString(R.string.daily_reminder_enabled))
                dailyReminderReceiver?.setDailyReminder(context!!)
            }else{
                showMessage(resources.getString(R.string.daily_reminder_disabled))
                dailyReminderReceiver?.cancelReminder(context!!)
            }
        }else if(prefKey.equals(KEY_RELEASE_REMINDER) && context != null){
            if(isActive){
                showMessage(resources.getString(R.string.release_reminder_enabled))
                releaseReminderReceiver?.setReleaseReminder(context!!)
            }else{
                showMessage(resources.getString(R.string.release_reminder_disabled))
                releaseReminderReceiver?.cancelReminder(context!!)
            }
        }
        return true
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }

}