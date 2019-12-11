package com.hast.norvialle.gui.main

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.hast.norvialle.R
import com.hast.norvialle.data.Event
import com.hast.norvialle.gui.BaseActivity
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.SettingsActivity
import com.hast.norvialle.gui.calendar.CalendarActivity
import com.hast.norvialle.gui.contacts.ContactsListActivity
import com.hast.norvialle.gui.dresses.DressesListActivity
import com.hast.norvialle.gui.makeup.MakeupListActivity
import com.hast.norvialle.gui.studio.StudiosListActivity
import com.hast.norvialle.utils.notifications.AlarmReceiver
import com.hast.norvialle.utils.notifications.createNotificationChannels
import com.hast.norvialle.utils.notifications.deleteAlarmForEvent
import com.hast.norvialle.utils.showDeleteDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val SCROLL_TO = "SCROLL_TO"
    }

    val presenter: MainPresenter =
        MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        list.layoutManager = LinearLayoutManager(this)



        createNotificationChannels(this)

        swipeRefresh.setOnRefreshListener {
            val adapter = list.adapter as EventsAdapter
            adapter.showPrevious()
            swipeRefresh.isRefreshing = false
        }


    }

    override fun onResume() {
        super.onResume()
        prepareAdapter()
        if (intent != null) {
            val dateToScroll = intent.getLongExtra(SCROLL_TO, 0)
            list.smoothScrollToPosition(
                (list.adapter as EventsAdapter).getPositionByDate(
                    dateToScroll
                )
            )
        }
    }

    fun prepareAdapter() {
        val adapter = EventsAdapter(
            MainPresenter.events,
            this
        )
        list.adapter = adapter
        adapter.onAddEventListener =
            EventsAdapter.OnAddEventListener {
                openEventEditor(it)
            }
        adapter.onDeleteEventListener =
            EventsAdapter.OnAddEventListener { event ->
                showDeleteDialog(this, R.string.delete_dialog_event) {
                    deleteAlarmForEvent(this, event)
                    MainPresenter.delete(event)
                    prepareAdapter()
                }
            }
    }

    fun openEventEditor(event: Event) {
        val intent = Intent(this, AddEventActivity::class.java)
        intent.putExtra(AddEventActivity.EVENT_EXTRA, event)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { // Handle navigation view item clicks here.
        when (item.getItemId()) {
            R.id.calendar -> {
                startActivity(Intent(this, CalendarActivity::class.java))
            }
            R.id.contact -> {
                startActivity(Intent(this, ContactsListActivity::class.java))
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

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.main, menu);
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {

            R.id.addEvent -> {
                openEventEditor(
                    Event("", "", System.currentTimeMillis())
                )
            }

        }
        return super.onOptionsItemSelected(item);
    }


}
