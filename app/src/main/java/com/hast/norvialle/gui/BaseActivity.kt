package com.hast.norvialle.gui

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.gui.events.EventsList


/**
 * Created by Konstantyn Zakharchenko on 11.12.2019.
 */
abstract class BaseActivity : AppCompatActivity(){

    fun openEventsList(dateToScroll: Long = 0L) {

        showFragment(EventsList.newInstance(dateToScroll))
    }

    fun showFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .addToBackStack("Fragments")
            .replace(R.id.contentLayout, fragment).commitAllowingStateLoss()
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item);
    }
    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount != 0){
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }

    }

    fun setToolBarTitle(@StringRes title: Int) {
        supportActionBar!!.title = getString(title)
    }

    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(getMenuRes(), menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        getMenuInflater().inflate(getMenuRes(), menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getMenuTitleRes())
    }
    abstract fun getMenuRes():Int
    abstract fun getMenuTitleRes(): Int


}