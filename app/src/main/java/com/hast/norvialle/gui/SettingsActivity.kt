package com.hast.norvialle.gui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.Settings
import com.hast.norvialle.utils.getTime
import com.hast.norvialle.utils.notifications.AlarmReceiver
import com.hast.norvialle.utils.notifications.resetAlarmsForEvents
import com.hast.norvialle.utils.notifications.resetDayBeforeAlarm
import com.hast.norvialle.utils.notifications.resetDaySameAlarm
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

        val settings = MainPresenter.settings
        dayBefore.isChecked = settings.notifyDayBefore
        thisDay.isChecked = settings.notifySameDay
        timeBeforeShoot.isChecked = settings.notifyTimeBefore

        defaultDayBeforeTime.setText(getTime(settings.timeOfDayBefore))
        defaultThisDayTime.setText(getTime(settings.timeOfSameDay))
        defaultTimeBeforeShoot.setText(getTime(settings.timeBeforeShoot))

        defaultDayBeforeTime.setOnClickListener { showTimePickerDialog(this, defaultDayBeforeTime) }
        defaultThisDayTime.setOnClickListener { showTimePickerDialog(this, defaultThisDayTime) }
        defaultTimeBeforeShoot.setOnClickListener {
            showTimePickerDialog(
                this,
                defaultTimeBeforeShoot
            )
        }
        wheels.isChecked = settings.useWheelsInput
        keyboard.isChecked = !settings.useWheelsInput
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

                if (settings.notifyDayBefore) {
                    val defaultDayBeforeTimeValue = getTime(defaultDayBeforeTime)
                    if (settings.timeOfDayBefore != defaultDayBeforeTimeValue) {
                        settings.timeOfDayBefore = defaultDayBeforeTimeValue
                        resetDayBeforeAlarm(this, settings)
                    }
                } else{
                    //TODO: Delete alarms
                }
                val defaultThisDayTimeValue = getTime(defaultThisDayTime)
                if (settings.timeOfSameDay != defaultThisDayTimeValue){
                settings.timeOfSameDay = defaultThisDayTimeValue
                    resetDaySameAlarm(this, settings)
            }


                val defaultTimeBeforeShootValue = getTime(defaultTimeBeforeShoot)
                if (settings.timeBeforeShoot != defaultTimeBeforeShootValue) {
                    settings.timeBeforeShoot = defaultTimeBeforeShootValue
                    resetAlarmsForEvents(this, MainPresenter.events, settings)
                }
               settings.useWheelsInput = wheels.isChecked
                MainPresenter.saveSettings(settings)
                finish()
            }

        }
        return super.onOptionsItemSelected(item);
    }

}