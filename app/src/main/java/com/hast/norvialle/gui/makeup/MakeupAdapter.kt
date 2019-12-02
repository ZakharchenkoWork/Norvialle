package com.hast.norvialle.gui.makeup

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hast.norvialle.R
import com.hast.norvialle.data.MakeupArtist
import com.hast.norvialle.data.PhotoRoom
import com.hast.norvialle.data.Studio
import kotlinx.android.synthetic.main.item_makeup.view.*
import kotlinx.android.synthetic.main.item_room_read_only.view.*
import kotlinx.android.synthetic.main.item_studio.view.*
import kotlinx.android.synthetic.main.item_studio.view.delete
import kotlinx.android.synthetic.main.item_studio.view.edit


class MakeupAdapter(val items: ArrayList<MakeupArtist>, private val context: Context) :
    RecyclerView.Adapter<MakeupAdapter.BaseViewHolder<*>>() {

    var onEditMakeupArtistListener: OnEditMakeupArtistListener =
        OnEditMakeupArtistListener {}
    var onDeleteMakeupArtistListener: OnEditMakeupArtistListener =
        OnEditMakeupArtistListener {}
    var onPickMakeupArtistListener: OnPickMakeupArtistListener =
        OnPickMakeupArtistListener{ makeupArtist : MakeupArtist -> }


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        (holder as MakeupViewHolder).bind(items[position])
    }

    var isForResult = false

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
            itemView.makeupArtistPhone.setOnClickListener { dial(makeupArtist.phone)}
            itemView.makeupArtistPhone.setTextColor(context.resources.getColor(R.color.blue))

            itemView.makeupArtistPrice.setText(context.getString(R.string.makeup_artist_price, makeupArtist.defaultPrice))


            if(!isForResult) {
                itemView.edit.setOnClickListener {
                    onEditMakeupArtistListener.doAction(makeupArtist)
                }
                itemView.delete.setOnClickListener { onDeleteMakeupArtistListener.doAction(makeupArtist) }
            } else{
                itemView.edit.visibility = View.GONE
                itemView.delete.visibility = View.GONE
                itemView.setOnClickListener { onPickMakeupArtistListener.doAction(makeupArtist) }
            }
        }
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


    class OnEditMakeupArtistListener(private val onEditMakeupArtistListener: (makeupArtist : MakeupArtist) -> Unit) {
        fun doAction(makeupArtist : MakeupArtist) {
            onEditMakeupArtistListener(makeupArtist)
        }
    }

    class OnPickMakeupArtistListener(private val onPickMakeupArtistListener: (makeupArtist : MakeupArtist) -> Unit) {
        fun doAction(makeupArtist : MakeupArtist) {
            onPickMakeupArtistListener(makeupArtist)
        }
    }
}


