package com.hast.norvialle.presenataion.base.settings

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.Settings
import com.hast.norvialle.domain.MainPresenter
import com.hast.norvialle.utils.SETTING_AUTO_LOGIN
import com.hast.norvialle.utils.SETTING_FINGERPRINT
import com.hast.norvialle.utils.getTime
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

        autoSignIn.isChecked = getSharedPreferences(packageName, Context.MODE_PRIVATE).getBoolean(
            SETTING_AUTO_LOGIN, false)
        val isFingerprintChekedByDefault = getSharedPreferences(packageName, Context.MODE_PRIVATE).getBoolean(
            SETTING_FINGERPRINT, false
        )
        fingerprint.isChecked = isFingerprintChekedByDefault
        if (autoSignIn.isChecked){
            fingerprint.isEnabled = false
            fingerprint.isChecked = false
        }
        autoSignIn.setOnCheckedChangeListener{a , isChecked ->
            if(isChecked){
                fingerprint.isEnabled = false
                fingerprint.isChecked = false
            } else{
                fingerprint.isEnabled = true
                fingerprint.isChecked = isFingerprintChekedByDefault
            }
        }

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
                    resetAlarmsForEvents(this,
                        MainPresenter.events, settings)
                }
               settings.useWheelsInput = wheels.isChecked

                val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)

                prefs.edit().putBoolean(SETTING_FINGERPRINT,fingerprint.isChecked)
                    .putBoolean(SETTING_AUTO_LOGIN, autoSignIn.isChecked).apply()


                MainPresenter.saveSettings(settings)
                finish()
            }

        }
        return super.onOptionsItemSelected(item);
    }

}