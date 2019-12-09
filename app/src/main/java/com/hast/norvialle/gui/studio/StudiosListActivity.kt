package com.hast.norvialle.gui.studio


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.PhotoRoom
import com.hast.norvialle.data.Studio
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.studio.AddStudioActivity.Companion.STUDIO
import kotlinx.android.synthetic.main.activity_search_list.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class StudiosListActivity : AppCompatActivity() {
    companion object {
        val IS_FOR_RESULT = "for result"
    }

    val presenter: MainPresenter =
        MainPresenter
    var isForResult = false
    var isSearchShown = false

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
            getSupportActionBar()?.setTitle(R.string.studios);
        }
        searchFilter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                (list.adapter as StudiosAdapter).filterBy(""+p0)
            }
        })
    }


    override fun onResume() {
        super.onResume()
        prepareAdapter()
    }
    fun prepareAdapter() {
        val adapter = StudiosAdapter(MainPresenter.studios, this)
        adapter.isForResult = isForResult
        list.adapter = adapter
        if (!isForResult) {
            adapter.onEditStudioListener =
                StudiosAdapter.OnEditStudioListener { openStudioEditor(it) }
            adapter.onDeleteStudioListener = StudiosAdapter.OnEditStudioListener {
                MainPresenter.deleteStudio(it)
                prepareAdapter()
            }
        } else {
            adapter.onPickStudioListener = StudiosAdapter.OnPickStudioListener {
                    studio: Studio, photoRoom: PhotoRoom ->
                val finishIntent = Intent()
                finishIntent.putExtra("STUDIO", studio)
                finishIntent.putExtra("ROOM", photoRoom)
                setResult(Activity.RESULT_OK, finishIntent)
                finish()
            }
        }
    }

    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_search_plus, menu);
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
            }
            R.id.plus -> {
                openStudioEditor(Studio())
            } R.id.search-> {
                isSearchShown = !isSearchShown
            searchFilter.visibility = if (isSearchShown) View.VISIBLE else View.GONE
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun openStudioEditor(studio: Studio) {
        var intent = Intent(this, AddStudioActivity::class.java)
        intent.putExtra(STUDIO, studio)
        startActivityForResult(intent, AddStudioActivity.EDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AddStudioActivity.EDIT -> {
                if (data != null && data.extras != null && data?.extras?.getSerializable(STUDIO) != null) {
                    var studio = data?.extras?.getSerializable(STUDIO) as Studio
                    presenter.addStudio(studio)
                    prepareAdapter()
                    Log.d("result", "is " + resultCode + " name: " + studio.name)
                }
            }
        }
    }
}