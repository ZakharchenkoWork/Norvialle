package com.hast.norvialle.gui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hast.norvialle.Event
import com.hast.norvialle.R
import com.hast.norvialle.utils.getDate
import com.hast.norvialle.utils.getMillis
import com.hast.norvialle.utils.getTime
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_event.view.*

class EventsAdapter(val items: ArrayList<Event>, private val context: Context) : RecyclerView.Adapter<EventsAdapter.BaseViewHolder<*>>() {
    var dates: ArrayList<String>
    var onAddEventListener: OnAddEventListener =
        OnAddEventListener {}
    var onDeleteEventListener: OnAddEventListener =
        OnAddEventListener {}

    init {
        dates = prepareDates()

    }

    companion object {
        private const val TYPE_DATE = 0
        private const val TYPE_EVENT = 1

    }


    private fun prepareDates(): ArrayList<String> {
        val dates = ArrayList<String>()
        for (item in items) {
            val date = getDate(item.time)
            if (!dates.contains(date)) {
                dates.add(date)
            }
        }

        return dates
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

        return items.size + dates.size
    }

    override fun getItemViewType(position: Int): Int {
        var date: String = dates[0]
        var counter: Int = 0
        if (position == 0) {
            return TYPE_DATE
        }

        for (item in items) {

            if (!date.equals(getDate(item.time))) {
                counter++
                date = getDate(item.time)
                if (counter == position) {
                    return TYPE_DATE
                } else {
                    counter++
                    if (counter == position) {
                        return TYPE_EVENT
                    }
                }
            } else {
                counter++
                if (counter == position) {
                    return TYPE_EVENT
                }
            }
        }

        return TYPE_EVENT
    }

    private fun getItemByPosition(position: Int): Any {
        var date: String = dates[0]
        var counter: Int = 0
        if (position == 0) {
            return date
        }

        for (item in items) {

            if (!date.equals(getDate(item.time))) {
                counter++
                date = getDate(item.time)
                if (counter == position) {
                    return date
                } else {
                    counter++
                    if (counter == position) {
                        return item
                    }
                }
            } else {
                counter++
                if (counter == position) {
                    return item
                }
            }
        }
        return Event()
    }


    inner class DateViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

        override fun bind(item: String) {
            itemView.date.setText(item)
            itemView.plus.setOnClickListener { onAddEventListener.doAction(getMillis(item)) }
        }
    }

    inner class EventViewHolder(itemView: View) : BaseViewHolder<Event>(itemView) {

        override fun bind(event: Event) {
            itemView.name.setText(event.name)
            itemView.time.setText(getTime(event.time))
            itemView.description.setText(event.description)
            itemView.studio.visibility = if(event.orderStudio) View.VISIBLE else View.GONE
            itemView.dress.visibility = if(event.orderDress) View.VISIBLE else View.GONE
            itemView.makeup.visibility = if(event.orderMakeup) View.VISIBLE else View.GONE


            itemView.foregroundLayout.setOnClickListener {
                itemView.foregroundLayout.animate()
                    .translationX(-itemView.foregroundLayout.width.toFloat())
                    .setDuration(150)
            }
            itemView.arrow.setOnClickListener {
                itemView.foregroundLayout.animate()
                    .translationX(0f)
                    .setDuration(150)
            }

            if (!event.link.equals("")) {
                itemView.insta.setOnClickListener {
                    val uri = Uri.parse(event.link)
                    val likeIng = Intent(Intent.ACTION_VIEW, uri)
                    likeIng.setPackage("com.instagram.android")

                    try {
                        itemView.context.startActivity(likeIng)
                    } catch (e: ActivityNotFoundException) {
                        itemView.context.startActivity(
                            Intent(Intent.ACTION_VIEW, uri)
                        )
                    }
                }
            } else {
                itemView.insta.visibility = View.GONE
            }



            itemView.copy.setOnClickListener { onAddEventListener.doAction(event.copy()) }
            itemView.edit.setOnClickListener { onAddEventListener.doAction(event) }
            itemView.delete.setOnClickListener{ onDeleteEventListener.doAction(event)}

        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }


    class OnAddEventListener(private val onAddeventListener: (event: Event) -> Unit) {
        fun doAction(date: Long) {
            onAddeventListener(Event("", "", date))
        }

        fun doAction(event: Event) {
            onAddeventListener(event)
        }
    }
}


