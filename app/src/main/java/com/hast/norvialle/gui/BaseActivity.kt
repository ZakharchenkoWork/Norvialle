package com.hast.norvialle.gui

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.AuthData
import com.hast.norvialle.gui.events.EventsListFragment
import com.hast.norvialle.utils.SAVED_LOGIN
import com.hast.norvialle.utils.SAVED_PASSWORD


/**
 * Created by Konstantyn Zakharchenko on 11.12.2019.
 */
abstract class BaseActivity : AppCompatActivity(){
companion object {
    val NO_VIEW = 0
}
    fun openEventsList(dateToScroll: Long = 0L) {

        showFragment(EventsListFragment.newInstance(dateToScroll))
    }

    fun showFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
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
        supportActionBar?.title = getString(title)
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
        setToolBarTitle(getMenuTitleRes())
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getMenuTitleRes())
    }
    abstract fun getMenuRes():Int
    abstract fun getMenuTitleRes(): Int

    fun stringPriceWithPlaceholder(value: String, placeholder : String) : String{
        return if (!value.equals("") && !value.equals("0") ) {
            return value
        } else{
            return placeholder
        }
    }
    fun stringPriceWithPlaceholder(value: String, placeholder : Int) : String{
        return stringPriceWithPlaceholder(value, getString(placeholder))
    }
    fun saveData(key: String, data: String) {
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        prefs.edit().putString(key, data).apply()
    }

    fun saveData(key: String, data: Boolean) {
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, data).apply()
    }

    fun loadData(key: String, default: Boolean = false) : Boolean {
        return getSharedPreferences(packageName, Context.MODE_PRIVATE).getBoolean(key, default)
    }
    fun loadAuthData() : AuthData {
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        return AuthData(prefs.getString(SAVED_LOGIN,"")?:"",
            prefs.getString(SAVED_PASSWORD, "")?:"")
    }
    fun clearAuthData(){
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(SAVED_LOGIN, "")
            .putString(SAVED_PASSWORD, "")
            .apply()

    }
    fun saveAuthData(authData: AuthData){
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(SAVED_LOGIN, authData.login)
            .putString(SAVED_PASSWORD, authData.password)
            .apply()
    }
}