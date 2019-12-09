package com.hast.norvialle.gui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.Settings
import com.hast.norvialle.utils.getTime
import com.hast.norvialle.utils.notifications.AlarmReceiver
import com.hast.norvialle.utils.showTimePickerDialog
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * Created by Konstantyn Zakharchenko on 05.12.2019.
 */
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar)
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar()?.setTitle(R.string.adding_event);
        }

        dayBefore.isChecked = MainPresenter.settings.notifyDayBefore
        thisDay.isChecked = MainPresenter.settings.notifySameDay
        timeBeforeShoot.isChecked = MainPresenter.settings.notifyTimeBefore

        defaultDayBeforeTime.setText(getTime(MainPresenter.settings.timeOfDayBefore))
        defaultThisDayTime.setText(getTime(MainPresenter.settings.timeOfSameDay))
        defaultTimeBeforeShoot.setText(getTime(MainPresenter.settings.timeBeforeShoot))

        defaultDayBeforeTime.setOnClickListener { showTimePickerDialog(this, defaultDayBeforeTime) }
        defaultThisDayTime.setOnClickListener { showTimePickerDialog(this, defaultThisDayTime) }
        defaultTimeBeforeShoot.setOnClickListener {
            showTimePickerDialog(
                this,
                defaultTimeBeforeShoot
            )
        }
        AlarmReceiver().setAlarms(this, MainPresenter.events, MainPresenter.settings, false)
//        defaultDayBeforeTime
    }

    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
            }
            R.id.save -> {
                val settings: Settings = Settings()
                settings.notifyDayBefore = dayBefore.isChecked
                settings.notifySameDay = thisDay.isChecked
                settings.notifyTimeBefore = timeBeforeShoot.isChecked

                settings.timeOfDayBefore = getTime(defaultDayBeforeTime)
                settings.timeOfSameDay = getTime(defaultThisDayTime)
                settings.timeBeforeShoot = getTime(defaultTimeBeforeShoot)
                MainPresenter.saveSettings(settings)
                finish()
            }

        }
        return super.onOptionsItemSelected(item);
    }

}