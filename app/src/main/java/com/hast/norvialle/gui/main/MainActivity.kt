package com.hast.norvialle.gui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.hast.norvialle.R
import com.hast.norvialle.data.Event
import com.hast.norvialle.gui.BaseActivity
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.SettingsActivity

import com.hast.norvialle.gui.calendar.CalendarFragment
import com.hast.norvialle.gui.contacts.ContactsListFragment
import com.hast.norvialle.gui.dresses.DressesListActivity
import com.hast.norvialle.gui.events.AddEventActivity
import com.hast.norvialle.gui.makeup.MakeupListActivity
import com.hast.norvialle.gui.studio.StudiosListActivity
import com.hast.norvialle.utils.notifications.createNotificationChannels
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun getMenuRes(): Int {
        return R.menu.main
    }
    override fun getMenuTitleRes(): Int {
        return R.string.app_name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setSupportActionBar(toolbar)
        val toggle =
            ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        drawerLayout.closeDrawer(GravityCompat.START)


        MainPresenter.start()
        navigationView.setNavigationItemSelectedListener(this)

        createNotificationChannels(this)

        openEventsList()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { // Handle navigation view item clicks here.
        drawerLayout.closeDrawer(GravityCompat.START)
        drawerLayout.postDelayed({
            when (item.getItemId()) {
                R.id.calendar -> {
                    showFragment(CalendarFragment.newInstance())
                }
                R.id.contact -> {
                    showFragment(ContactsListFragment.newInstance())
                }
                R.id.studio -> {
                    startActivity(Intent(this, StudiosListActivity::class.java))
                }
                R.id.makeup -> {
                    startActivity(Intent(this, MakeupListActivity::class.java))
                }
                R.id.dress -> {
                    startActivity(Intent(this, DressesListActivity::class.java))
                }
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.eventsList -> {
                    openEventsList()
                }
                R.id.addEvent -> {
                    openEventEditor(
                        Event(
                            "",
                            "",
                            System.currentTimeMillis()
                        )
                    )
                }
            }
            }, 100L)

        return true
    }



    fun openEventEditor(event: Event) {
        val intent = Intent(this, AddEventActivity::class.java)
        intent.putExtra(AddEventActivity.EVENT_EXTRA, event)
        startActivity(intent)
    }

}
