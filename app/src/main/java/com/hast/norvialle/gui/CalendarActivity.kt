package com.hast.norvialle.gui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class CalendarActivity : AppCompatActivity(){
    val presenter: MainPresenter = MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
    }
}