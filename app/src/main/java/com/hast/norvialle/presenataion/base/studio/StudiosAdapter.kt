package com.hast.norvialle.presenataion.base.studio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hast.norvialle.R
import com.hast.norvialle.data.Studio
import com.hast.norvialle.presenataion.base.BaseAdapter
import kotlinx.android.synthetic.main.item_room_read_only.view.*
import kotlinx.android.synthetic.main.item_studio.view.*


class StudiosAdapter(allItems: ArrayList<Studio>, context: Context) :
    BaseAdapter<Studio>(allItems, context) {


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


    inner class StudioViewHolder(itemView: View) : BaseViewHolder<Studio>(itemView) {

        override fun bind(studio: Studio) {
            itemView.studioName.setText(studio.name)
            itemView.address.setText(studio.address)
            if (!studio.link.equals("")) {
                if (!isForResult) {
                    itemView.address.setOnClickListener { open2gis(studio.link) }
                }
                itemView.address.setTextColor(context.resources.getColor(R.color.blue))
            } else {
                itemView.address.setOnClickListener { }
                itemView.address.setTextColor(context.resources.getColor(R.color.black))
            }
            if (!studio.phone.equals("") && !isForResult) {
                itemView.studioPhone.setText(studio.phone)
                itemView.studioPhone.visibility = View.VISIBLE
                itemView.call.visibility = View.VISIBLE
                itemView.call.setOnClickListener { dial(studio.phone) }
            } else {
                itemView.studioPhone.visibility = View.GONE
                itemView.call.visibility = View.GONE
            }
            if(!isForResult) {
                itemView.edit.setOnClickListener {
                    onEditistener?.invoke(studio)
                }
                itemView.delete.setOnClickListener { onDeleteListener?.invoke(studio) }
            } else{
                itemView.edit.visibility = View.GONE
                itemView.delete.visibility = View.GONE
            }

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
                            if (isForResult){
                                view.setOnClickListener { onPickListenerWithOptional?.invoke(studio, room) }
                            } else{
                                view.setOnClickListener{}
                            }
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

    override fun isMatchingFilter(data: Studio, filterText: String): Boolean {

     return contains(data.name, filterText) || contains(data.address, filterText) ||
                    checkPhone(data.phone, filterText)
                    || data.roomsContains(filterText)

    }

}


