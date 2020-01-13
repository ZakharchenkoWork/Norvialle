package com.hast.norvialle.presenataion.base

import android.app.Activity
import android.content.*
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.AuthData
import com.hast.norvialle.data.PhoneContact
import com.hast.norvialle.presenataion.base.events.EventsListFragment
import com.hast.norvialle.presenataion.utils.InputView
import com.hast.norvialle.utils.SAVED_LOGIN
import com.hast.norvialle.utils.SAVED_PASSWORD


/**
 * Created by Konstantyn Zakharchenko on 11.12.2019.
 */
abstract class BaseActivity : AppCompatActivity() {
    companion object {
        const val NO_VIEW = 0
    }

    fun openEventsList(dateToScroll: Long = 0L) {
        showFragment(EventsListFragment.newInstance(dateToScroll))
    }

    fun showFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.animator.fade_in,
                android.R.animator.fade_out,
                android.R.animator.fade_in,
                android.R.animator.fade_out
            )
            .addToBackStack("Fragments")
            .replace(R.id.contentLayout, fragment).commitAllowingStateLoss()
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount != 0) {
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

        val menuRes = getMenuRes()
        if (menuRes != 0) {
            menuInflater.inflate(menuRes, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val title = getMenuTitleRes()
        if (title != 0) {
            menu.clear()
            menuInflater.inflate(getMenuRes(), menu)

            setToolBarTitle(title)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getMenuTitleRes())
    }

    abstract fun getMenuRes(): Int
    abstract fun getMenuTitleRes(): Int

    fun stringPriceWithPlaceholder(value: String, placeholder: String): String {
        return if (value.isNotEmpty() && value != "0") {
            value
        } else {
            placeholder
        }
    }

    fun stringPriceWithPlaceholder(value: String, placeholder: Int): String {
        return stringPriceWithPlaceholder(value, getString(placeholder))
    }

    /*fun saveData(key: String, data: String) {
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        prefs.edit().putString(key, data).apply()
    }*/

    fun saveData(key: String, data: Boolean) {
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, data).apply()
    }

    fun loadData(key: String, default: Boolean = false): Boolean {
        return getSharedPreferences(packageName, Context.MODE_PRIVATE).getBoolean(key, default)
    }

    fun loadAuthData(): AuthData {
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        return AuthData(
            prefs.getString(SAVED_LOGIN, "") ?: "",
            prefs.getString(SAVED_PASSWORD, "") ?: ""
        )
    }

    fun clearAuthData() {
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(SAVED_LOGIN, "")
            .putString(SAVED_PASSWORD, "")
            .apply()

    }

    fun saveAuthData(authData: AuthData) {
        val prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(SAVED_LOGIN, authData.login)
            .putString(SAVED_PASSWORD, authData.password)
            .apply()
    }


    fun getResolvedColor(colorRes: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.resources.getColor(colorRes, null)
        } else {
            @Suppress("DEPRECATION")
            this.resources.getColor(colorRes)
        }
    }

    class InvalidSavingData(message: String) : RuntimeException(message)

    inline fun <reified T> checkToSave(view: InputView): T {
        if (view.validationCheck()) {
            return when (T::class) {
                Int::class -> {
                    view.getIntValue() as T
                }
                String::class -> {
                    view.getText() as T
                }
                else -> {
                    throw InvalidSavingData(
                        getString(R.string.input_error, view.getHint())
                    )
                }
            }
        }
        throw InvalidSavingData(
            getString(R.string.input_error, view.getHint())
        )
    }
    fun checkField(view: InputView) {
        if (!view.validationCheck()) {
            throw InvalidSavingData(getString(R.string.input_error, view.getHint()))
        }
    }

    fun fastToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun fastToast(dataCheckFailed: InvalidSavingData) {
        dataCheckFailed.message?.let { fastToast(it) }
    }
   inline fun <reified Type>getIntentData(tag : String) : Type?{

        val extras = intent.extras
        if (extras != null) {
            val serializable = extras.getSerializable(tag)
            if (serializable is Type)
            return serializable
        }

       return null
    }

    fun pasteLinkedInLink() : String?{
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val primaryClipDescription = clipboard.primaryClipDescription
        if (!clipboard.hasPrimaryClip() || primaryClipDescription == null || !primaryClipDescription.hasMimeType(
                ClipDescription.MIMETYPE_TEXT_PLAIN
            )
        ) {
            return null
        } else {
            val primaryClip = clipboard.primaryClip
            if (primaryClip != null) {
                val item: ClipData.Item = primaryClip.getItemAt(0)
                return item.text.toString()
            }
        }
        return null
    }

    fun extractContact(data: Intent): PhoneContact {
        val contactUri = data.data
        if (contactUri != null) {
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            val cursor: Cursor? = contentResolver
                .query(contactUri, projection, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val columnPhone: Int =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val columnName: Int =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                val phoneContact =
                    PhoneContact(cursor.getString(columnName), cursor.getString(columnPhone))
                cursor.close()
                return phoneContact
            }
        }
        return PhoneContact("", "")
    }
    fun isResultDataOk(resultCode : Int, receivedCode : Int, requestCode : Int): Boolean{
        return resultCode == Activity.RESULT_OK && receivedCode == requestCode
    }

    fun finishWithOkResult() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}