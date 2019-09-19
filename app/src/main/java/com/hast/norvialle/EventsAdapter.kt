package com.hast.norvialle

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_event.view.*

class EventsAdapter(
    val items: ArrayList<EventList>, private val context: Context
) : RecyclerView.Adapter<EventsAdapter.BaseViewHolder<*>>() {

    companion object {
        private const val TYPE_DATE = 0
        private const val TYPE_EVENT = 1

    }

    override fun getItemViewType(position: Int): Int {

        var counter: Int = 0
        for (item in items) {

            if (position == counter) {
                return TYPE_DATE
            }
            for (event in item.events) {
                counter++
                if (position == counter) {
                    return TYPE_EVENT
                }
            }
            counter++
        }
        return super.getItemViewType(position)
    }


    private fun getItemByPosition(position: Int): Any {
        var counter: Int = 0
        for (item in items) {

            if (position == counter) {
                return item.date
            }
            for (event in item.events) {
                counter++
                if (position == counter) {
                    return event
                }
            }
            counter++

        }
        return ""
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = getItemByPosition(position)
        when (holder) {
            is DateViewHolder -> holder.bind(element as String)
            is EventViewHolder -> holder.bind(element as Event)
            else -> throw IllegalArgumentException()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        return when (viewType) {
            TYPE_DATE -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_date, parent, false)
                DateViewHolder(view)
            }
            TYPE_EVENT -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_event, parent, false)
                EventViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun getItemCount(): Int {
        var counter: Int = 0
        for (item in items) {
            counter++
            for (event in item.events) {
                counter++
            }
        }
        return counter
    }

    inner class DateViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

        override fun bind(item: String) {
            itemView.date.setText(item)
        }
    }

    inner class EventViewHolder(itemView: View) : BaseViewHolder<Event>(itemView) {

        override fun bind(item: Event) {
            itemView.name.setText(item.name)
            itemView.time.setText(item.time)
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }
}


