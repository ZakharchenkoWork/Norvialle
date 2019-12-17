package com.hast.norvialle.gui.events

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.Event
import com.hast.norvialle.gui.BaseFragment
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.utils.notifications.deleteAlarmForEvent
import com.hast.norvialle.utils.showDeleteDialog
import kotlinx.android.synthetic.main.fragment_list.view.*


/**
 * Created by Konstantyn Zakharchenko on 11.12.2019.
 */
class EventsListFragment : BaseFragment() {

    var dateToScroll = 0L

    companion object {
        fun newInstance(dateToScroll: Long = 0L): BaseFragment {
            val eventsList = EventsListFragment()
            eventsList.dateToScroll = dateToScroll
            return eventsList
        }
    }

    override fun getContentId(): Int {
        return R.layout.fragment_list
    }

    override fun getMenuId(): Int {
        return R.menu.menu_plus
    }

    override fun getMenuTitleId(): Int {
        return R.string.app_name
    }

    override fun onCreateView(root: View): View {
        root.list.layoutManager = LinearLayoutManager(context)
        root.swipeRefresh.setOnRefreshListener {
            val adapter = root.list.adapter as EventsAdapter
         //   adapter.showPrevious()
            root.swipeRefresh.isRefreshing = false
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        val context = this.context
        if (context != null) {
            prepareAdapter(context)

            root.list.smoothScrollToPosition(
                (root.list.adapter as EventsAdapter).getPositionByDate(
                    dateToScroll
                )
            )
        }
    }

    fun prepareAdapter(context: Context) {

        val adapter = EventsAdapter(
            MainPresenter.events,
            context
        )
        root.list.adapter = adapter
        adapter.onAddEventListener =
            EventsAdapter.OnAddEventListener {
                openEventEditor(it)
            }
        adapter.onDressClickListener = {event ->
            openDressList(event.dresses) { newPickedDresses ->
                event.dresses = newPickedDresses
                MainPresenter.addEvent(event)
                adapter.updateItem(event)
            }
        }
        adapter.onDeleteEventListener =
            EventsAdapter.OnAddEventListener { event ->
                showDeleteDialog(context, R.string.delete_dialog_event) {
                    deleteAlarmForEvent(context, event)
                    MainPresenter.delete(event)
                    prepareAdapter(context)
                }
            }
    }

    fun openEventEditor(event: Event) {
        val intent = Intent(context, AddEventActivity::class.java)
        intent.putExtra(AddEventActivity.EVENT_EXTRA, event)
        startActivity(intent)
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.plus -> {
                openEventEditor(
                    Event("", "", System.currentTimeMillis())
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}