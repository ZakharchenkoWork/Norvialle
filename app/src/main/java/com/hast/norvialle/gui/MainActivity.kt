package com.hast.norvialle.gui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.hast.norvialle.Event
import com.hast.norvialle.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    val presenter: MainPresenter = MainPresenter

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


        presenter.start()
        navigationView.setNavigationItemSelectedListener(this)
        list.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()
        prepareAdapter()
    }

    fun prepareAdapter() {
        val adapter = EventsAdapter(presenter.events, this)
        list.adapter = adapter
        adapter.onAddEventListener = EventsAdapter.OnAddEventListener { openEventEditor(it) }
        adapter.onDeleteEventListener = EventsAdapter.OnAddEventListener { event ->
            presenter.delete(event)
            prepareAdapter()
        }
    }
    fun openEventEditor(event: Event){
        val intent = Intent(this, AddEventActivity::class.java)
        intent.putExtra(AddEventActivity.EVENT_EXTRA, event)
        startActivity(intent)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean { // Handle navigation view item clicks here.
        when (item.getItemId()) {
            R.id.addEvent-> {
                openEventEditor(Event("", "", System.currentTimeMillis()))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
