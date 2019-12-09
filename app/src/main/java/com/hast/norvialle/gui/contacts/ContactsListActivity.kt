package com.hast.norvialle.gui.contacts


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.Contact

import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.utils.getSearchTextWatcher
import kotlinx.android.synthetic.main.activity_search_list.*

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class ContactsListActivity : AppCompatActivity() {
    companion object {
        val IS_FOR_RESULT = "for result"
        val RETURN_DATA = "CONTACT"
    }

    val presenter: MainPresenter =
        MainPresenter
    var isForResult = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_list)
        setSupportActionBar(toolbar)
        isForResult = intent.getBooleanExtra(IS_FOR_RESULT, false)

        list.layoutManager = LinearLayoutManager(this)

        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar()?.setTitle(R.string.contacts);
        }

    }

    override fun onResume() {
        super.onResume()
        prepareAdapter()
    }
    fun prepareAdapter() {
        val adapter = ContactsAdapter(MainPresenter.contacts, this)
        adapter.isForResult = isForResult
        list.adapter = adapter
        if (!isForResult) {
            adapter.onEditistener = {
                openEditor(it)
                }
            adapter.onDeleteListener = {
                MainPresenter.deleteContact(it)
                prepareAdapter()
            }
        } else {
            adapter.onPickListener = {
                    contact : Contact ->
                val finishIntent = Intent()
                finishIntent.putExtra(RETURN_DATA, contact)
                setResult(Activity.RESULT_OK, finishIntent)
                finish()
            }
        }
        searchFilter.addTextChangedListener(getSearchTextWatcher { adapter.filterBy(it) })
    }

    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_search_plus, menu);
        return true
    }
    var isSearchShown = false
    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
            }
            R.id.plus -> {
                openEditor(Contact("","",""))
            }
            R.id.search-> {
            isSearchShown = !isSearchShown
            searchFilter.visibility = if (isSearchShown) View.VISIBLE else View.GONE
        }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun openEditor(contact : Contact) {
        var intent = Intent(this, AddContactActivity::class.java)
        intent.putExtra(AddContactActivity.DATA_TYPE, contact)
        startActivityForResult(intent, AddContactActivity.EDIT)
    }

}