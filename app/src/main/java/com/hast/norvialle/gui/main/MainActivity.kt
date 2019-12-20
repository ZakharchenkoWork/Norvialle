package com.hast.norvialle.gui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.hast.norvialle.R
import com.hast.norvialle.data.Event
import com.hast.norvialle.gui.BaseActivity
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.SettingsActivity
import com.hast.norvialle.gui.auth.AuthActivity
import com.hast.norvialle.gui.calendar.CalendarFragment
import com.hast.norvialle.gui.contacts.ContactsListFragment
import com.hast.norvialle.gui.dialogs.SimpleDialog
import com.hast.norvialle.gui.dresses.DressesListFragment
import com.hast.norvialle.gui.events.AddEventActivity
import com.hast.norvialle.gui.makeup.AssistantListFragment
import com.hast.norvialle.gui.makeup.MakeupListFragment
import com.hast.norvialle.gui.studio.StudiosListFragment
import com.hast.norvialle.utils.notifications.createNotificationChannels
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun getMenuRes(): Int {
        return R.menu.main
    }
    override fun getMenuTitleRes(): Int {
        return R.string.app_name
    }
inner class BarToogle(activity: Activity , drawerLayout : DrawerLayout,
                toolbar : Toolbar, openDrawerContentDescRes : Int,
                closeDrawerContentDescRes: Int) : ActionBarDrawerToggle(
    activity, drawerLayout,
     toolbar, openDrawerContentDescRes,
    closeDrawerContentDescRes){
   override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
      /*  if (slideOffset == 0f && actionBar.getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD) { // drawer closed

        } else if (slideOffset != 0f && actionBar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) { // started opening
        //TODO: close keyboard on drawer open
        }*/
        super.onDrawerSlide(drawerView, slideOffset)
    }

}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setSupportActionBar(toolbar)
        val toggle =
            BarToogle(
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
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { // Handle navigation view item clicks here.
        drawerLayout.closeDrawer(GravityCompat.START)
        drawerLayout.postDelayed({
            when (item.getItemId()) {
                R.id.calendar -> {
                    showFragment(CalendarFragment.newInstance())
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
                R.id.contact -> {
                    showFragment(ContactsListFragment.newInstance())
                }
                R.id.studio -> {
                    showFragment(StudiosListFragment.newInstance())
                }
                R.id.makeup -> {
                    showFragment(MakeupListFragment.newInstance())
                }
                R.id.assistant -> {
                    showFragment(AssistantListFragment.newInstance())
                }
                R.id.dress -> {
                    showFragment(DressesListFragment.newInstance())

                }
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.exit ->{
                    SimpleDialog(this, SimpleDialog.DIALOG_TYPE.MESSAGE_ONLY)
                        .setTitle(getString(R.string.exit))
                        .setMessage(getString(R.string.exit_warning))
                        .setOkButtonText(getString(R.string.delete_dialog_yes))
                        .setCancelButtonText(getString(R.string.delete_dialog_no))
                        .setCancelable(true)
                        .setOkListener{
                            logout()
                        }.build()

                }

            }
            }, 100L)

        return true
    }

    private fun logout() {
        clearAuthData()
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


    fun openEventEditor(event: Event) {
        val intent = Intent(this, AddEventActivity::class.java)
        intent.putExtra(AddEventActivity.EVENT_EXTRA, event)
        startActivity(intent)
    }

}
