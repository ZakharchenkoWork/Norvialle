package com.hast.norvialle.gui.events

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hast.norvialle.R
import com.hast.norvialle.data.Event
import com.hast.norvialle.utils.*
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_event.view.*


class EventsAdapter(val allItems: ArrayList<Event>, private val context: Context) :
    RecyclerView.Adapter<EventsAdapter.BaseViewHolder<*>>() {
    val items: ArrayList<Event> = ArrayList()
    var dates: ArrayList<String>
    var onAddEventListener: OnAddEventListener =
        OnAddEventListener {}
    var onDeleteEventListener: OnAddEventListener =
        OnAddEventListener {}
    var eventsBack = 0
    companion object {
        private const val TYPE_DATE = 0
        private const val TYPE_EVENT = 1

    }
    init {

        prepareItems()
        dates = prepareDates()

    }

    fun prepareItems() {
        val dateNow = getDateInMillis(System.currentTimeMillis())
        var firstIndex = -1
        for ((index, item) in allItems.withIndex()) {
            if (getDateInMillis(item.time) >= dateNow) {
                if (firstIndex == -1) {
                    firstIndex = index
                }
                items.add(item)
            }
        }
        firstIndex--
        if (firstIndex > 0) {

            var downValue = if (firstIndex - eventsBack >= 0) firstIndex - eventsBack else 0

            for (i in firstIndex downTo downValue){
                if (i >= 0) {
                    items.add(0, allItems[i])
                }
            }

        }
    }
    fun showPrevious() {
        items.clear()
        eventsBack += 10
        prepareItems()
        dates = prepareDates()
        notifyDataSetChanged()
    }



    private fun prepareDates(): ArrayList<String> {
        val dates = ArrayList<String>()
        for (item in items) {
            val date = getDateLocal(item.time)
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

            if (!date.equals(getDateLocal(item.time))) {
                counter++
                date = getDateLocal(item.time)
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

            if (!date.equals(getDateLocal(item.time))) {
                counter++
                date = getDateLocal(item.time)
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
        }
    }

    inner class EventViewHolder(itemView: View) : BaseViewHolder<Event>(itemView) {

        override fun bind(event: Event) {
            itemView.locationName.setText(event.studioName)
            itemView.locationRoom.setText(event.studioRoom)
            itemView.totalPrice.setText("" + event.totalPrice)
            itemView.paid.setText("" + event.paidPrice)
            itemView.moneyLeft.setText(context.getString(R.string.monyLeft, event.getMoneyLeft()))
            itemView.contact.setText(event.name)
            itemView.description.setText(event.description)



            itemView.phone.setText(event.contactPhone)
            if (!event.contactPhone.equals("")) {
                itemView.call.setOnClickListener { dial(event.contactPhone) }
                itemView.call.visibility = View.VISIBLE
                itemView.phone.visibility = View.VISIBLE
            } else {
                itemView.call.visibility = View.GONE
                itemView.phone.visibility = View.GONE
            }
            itemView.address.setText(event.studioAddress)

            if (!event.studioGeo.equals("")) {
                itemView.address.setTextColor(context.resources.getColor(R.color.blue))
                itemView.address.setOnClickListener { open2gis(event.studioGeo) }
                itemView.geo.visibility = View.VISIBLE
                itemView.geo.setOnClickListener { open2gis(event.studioGeo) }
            } else {
                itemView.address.setTextColor(context.resources.getColor(R.color.black))
                itemView.geo.visibility = View.GONE
            }

            itemView.time.setText(getTimeLocal(event.time))
            itemView.studio.setImageResource(if (event.orderStudio) R.drawable.studio else R.drawable.studio_disabled)
            itemView.dress.setImageResource(if (event.orderDress) R.drawable.dress else R.drawable.dress_disabled)
            if (event.orderMakeup) {
                itemView.makeup.setImageResource(R.drawable.makeup)
                itemView.makeupTime.visibility = View.VISIBLE
                itemView.makeupTime.setText(context.getString(R.string.makeup_label, getTimeLocal(event.makeupTime), event.makeupArtistName))
                if (!event.makeupPhone.equals("")) {
                    itemView.makeupTime.setTextColor(context.resources.getColor(R.color.blue))
                    itemView.makeupTime.setOnClickListener { dial(event.makeupPhone) }
                }
            }else{
                itemView.makeup.setImageResource(R.drawable.makeup_disabled)
                itemView.makeupTime.visibility = View.GONE
            }

            if (!event.studioPhone.equals("")) {
                itemView.studioPhone.setText(event.studioPhone)
                itemView.studioPhone.visibility = View.VISIBLE
                itemView.studioPhone.setTextColor(context.resources.getColor(R.color.blue))
                itemView.studioPhone.setOnClickListener { dial(event.studioPhone) }
            } else {
                itemView.studioPhone.visibility = View.VISIBLE

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
            itemView.delete.setOnClickListener {
               onDeleteEventListener.doAction(event)
            }

            itemView.foregroundLayout.setOnTouchListener(
                DragListener(
                    itemView.foregroundLayout,
                    true
                )
            )
            itemView.backgroundLayout.setOnTouchListener(
                DragListener(
                    itemView.foregroundLayout,
                    false
                )
            )
        }
    }

    inner class DragListener(val layout: View, val toLeft: Boolean) : View.OnTouchListener {
        var dragStartY: Float = -1f
        var dragStartX: Float = -1f
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                dragStartY = event.y
                dragStartX = event.x
                return true
            } else if (event.action == MotionEvent.ACTION_UP) {
                dragStartY = -1f
                dragStartX = -1f
            } else {
                if (event.y > dragStartY - 40 && event.y < dragStartY + 40) {
                    if (toLeft && event.x < dragStartX - 20) {
                        layout.animate()
                            .translationX(-layout.width.toFloat())
                            .setDuration(150)
                        return true
                    } else if (!toLeft && event.x > dragStartX + 20) {
                        layout.animate()
                            .translationX(0f)
                            .setDuration(150)
                        return true
                    }
                }
            }

            return false
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

    private fun open2gis(link: String) {
        val uri = Uri.parse(link.replace("http://", "dgis://"))

        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage("ru.dublgis.dgismobile") // Если не планируете работать с публичной бета-версией, эту строчку надо указать

        context.startActivity(intent)
    }

    private fun dial(phone: String) {

        context.startActivity(
            Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", phone, null)
            )
        )
    }

    fun getPositionByDate(dateToScroll: Long): Int {
        if (dateToScroll != 0L) {
            for (i in 1..itemCount) {
                val itemByPosition = getItemByPosition(i)
                if (itemByPosition is Event){
                    if(i == 1 && itemByPosition.time > dateToScroll){
                        if (items.size != allItems.size) {
                            showPrevious()
                            return getPositionByDate(dateToScroll)
                        } else{
                            return 0
                        }
                    }
                    if (getDateInMillis(itemByPosition.time) == dateToScroll) {
                        return i
                    }
                }
            }

            return 0
        } else{
            return 0
        }
    }


}


