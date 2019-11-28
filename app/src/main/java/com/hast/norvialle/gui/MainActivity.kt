package com.hast.norvialle.gui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.hast.norvialle.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val presenter: MainPresenter = MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()
        prepareAdapter()
    }

    fun prepareAdapter() {
        val adapter = EventsAdapter(presenter.events, this)
        list.adapter = adapter
        adapter.onAddEventListener = EventsAdapter.OnAddEventListener { event ->
            val intent = Intent(this, AddEventActivity::class.java)
            intent.putExtra(AddEventActivity.EVENT_EXTRA, event)
            startActivity(intent)
        }
        adapter.onDeleteEventListener = EventsAdapter.OnAddEventListener { event ->
            presenter.delete(event)
            prepareAdapter()
        }
    }
}
