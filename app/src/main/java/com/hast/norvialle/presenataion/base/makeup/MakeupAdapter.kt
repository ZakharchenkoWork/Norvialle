package com.hast.norvialle.presenataion.base.makeup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hast.norvialle.R
import com.hast.norvialle.data.MakeupArtist
import com.hast.norvialle.presenataion.base.BaseAdapter
import kotlinx.android.synthetic.main.item_makeup.view.*
import kotlinx.android.synthetic.main.item_studio.view.delete
import kotlinx.android.synthetic.main.item_studio.view.edit


class MakeupAdapter(allItems: ArrayList<MakeupArtist>, context: Context) :
    BaseAdapter<MakeupArtist>(allItems, context) {

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        (holder as MakeupViewHolder).bind(items[position])
    }

    override fun isMatchingFilter(data: MakeupArtist, filterText: String): Boolean {
        return contains(data.name, filterText) || checkPhone(data.phone, filterText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return MakeupViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_makeup,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return items.size
    }


    inner class MakeupViewHolder(itemView: View) : BaseViewHolder<MakeupArtist>(itemView) {

        override fun bind(makeupArtist: MakeupArtist) {
            itemView.makeupArtistName.setText(makeupArtist.name)
            itemView.makeupArtistPhone.setText(makeupArtist.phone)
            itemView.makeupArtistPhone.setOnClickListener { dial(makeupArtist.phone) }
            itemView.makeupArtistPhone.setTextColor(context.resources.getColor(R.color.blue))

            itemView.makeupArtistPrice.setText(
                context.getString(
                    R.string.makeup_artist_price,
                    makeupArtist.defaultPrice
                )
            )


            if (!isForResult) {
                itemView.edit.setOnClickListener {
                    onEditistener?.invoke(makeupArtist)
                }
                itemView.delete.setOnClickListener { onDeleteListener?.invoke(makeupArtist) }
            } else {
                itemView.edit.visibility = View.GONE
                itemView.delete.visibility = View.GONE
                itemView.setOnClickListener { onPickListener?.invoke(makeupArtist) }
            }
        }
    }
}


