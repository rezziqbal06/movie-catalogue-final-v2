package com.rezziqbal.moviecatalogue.ui.settingreminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rezziqbal.moviecatalogue.R

class ReminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        setFragmentReminder()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun setFragmentReminder() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.reminder, ReminderMovieFragment())
            .commit()
    }
}
