package com.hast.norvialle.gui.studio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.main.EventsAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class StudiosListActivity : AppCompatActivity() {
    val presenter: MainPresenter =
        MainPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studios_list)
        list.layoutManager = LinearLayoutManager(this)
        val adapter =
            StudiosAdapter(MainPresenter.events, this)
        list.adapter = adapter
        //adapter.onAddEventListener = EventsAdapter.OnAddEventListener { openEventEditor(it) }
        /*adapter.onDeleteEventListener = EventsAdapter.OnAddEventListener { event ->
            MainPresenter.delete(event)
            prepareAdapter()
        }*/
    }
}