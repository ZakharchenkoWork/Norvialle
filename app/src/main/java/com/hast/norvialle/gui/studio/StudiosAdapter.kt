package com.hast.norvialle.gui.studio

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.hast.norvialle.R
import com.hast.norvialle.data.Studio
import kotlinx.android.synthetic.main.item_room_read_only.view.*
import kotlinx.android.synthetic.main.item_studio.view.*


class StudiosAdapter(val items: ArrayList<Studio>, private val context: Context) :
    RecyclerView.Adapter<StudiosAdapter.BaseViewHolder<*>>() {

    var onEditStudioListener: OnEditStudioListener =
        OnEditStudioListener {}
    var onDeleteStudioListener: OnEditStudioListener =
        OnEditStudioListener {}


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        (holder as StudioViewHolder).bind(items[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return StudioViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_studio,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return items.size
    }


    inner class StudioViewHolder(itemView: View) : BaseViewHolder<Studio>(itemView) {

        override fun bind(studio: Studio) {
            itemView.studioName.setText(studio.name)
            itemView.address.setText(studio.address)
            if (!studio.link.equals("")) {
                itemView.address.setOnClickListener { open2gis(studio.link) }
                itemView.address.setTextColor(context.resources.getColor(R.color.blue))
            } else{
                itemView.address.setOnClickListener { }
                itemView.address.setTextColor(context.resources.getColor(R.color.black))
            }
            if (!studio.phone.equals("")) {
                itemView.studioPhone.setText(studio.phone)
                itemView.studioPhone.visibility = View.VISIBLE
                itemView.call.visibility = View.VISIBLE
                itemView.call.setOnClickListener { dial(studio.phone) }
            } else {
                itemView.studioPhone.visibility = View.GONE
                itemView.call.visibility = View.GONE
            }
            itemView.edit.setOnClickListener {
                onEditStudioListener.doAction(studio)
            }
            itemView.delete.setOnClickListener { onDeleteStudioListener.doAction(studio) }

            var areRoomsShown = false
            itemView.arrow.setOnClickListener {
                areRoomsShown = !areRoomsShown
                switchRoomsVisibility(itemView.arrow, areRoomsShown, studio)
            }
            itemView.setOnClickListener {
                areRoomsShown = !areRoomsShown
                switchRoomsVisibility(itemView.arrow, areRoomsShown, studio)
            }

        }

        private fun switchRoomsVisibility(
            arrow: ImageView,
            areRoomsShown: Boolean,
            studio: Studio
        ) {
            arrow.animate().rotationBy(180f).setDuration(150)

            if (areRoomsShown) {
                if (!studio.phone.equals("")) {
                    itemView.studioPhone.setText(studio.phone)
                    itemView.studioPhone.visibility = View.VISIBLE
                    itemView.call.visibility = View.VISIBLE
                } else {
                    itemView.studioPhone.visibility = View.GONE
                    itemView.call.visibility = View.GONE
                }
                if (!studio.rooms.isEmpty()) {
                    if (itemView.rooms.childCount == 0) {
                        for (room in studio.rooms) {
                            var view = LayoutInflater.from(context)
                                .inflate(R.layout.item_room_read_only, itemView.rooms, false)
                            itemView.rooms.addView(view)
                            view.roomName.setText(room.name)
                            view.price.setText("" + room.price)
                            view.priceWithDiscount.setText("" + room.priceWithDiscount)
                        }

                    }
                    itemView.rooms.visibility = View.VISIBLE
                }

            } else {
                itemView.rooms.visibility = View.GONE
                itemView.studioPhone.visibility = View.GONE
                itemView.call.visibility = View.GONE
            }
        }


    }

    private fun open2gis(link: String) {
        val uri = Uri.parse(link.replace("http://","dgis://"))

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

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }


    class OnEditStudioListener(private val onEditStudioListener: (studio: Studio) -> Unit) {
        fun doAction(studio: Studio) {
            onEditStudioListener(studio)
        }
    }
}


