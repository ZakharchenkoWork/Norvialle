package com.hast.norvialle.gui.events

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.hast.norvialle.R
import com.hast.norvialle.data.Dress
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.item_dress.view.*


class DressesPicturesAdapter(val items: ArrayList<Dress>, private val context: Context, val isSizeSmall : Boolean = false) :
    RecyclerView.Adapter<DressesPicturesAdapter.BaseViewHolder<*>>() {

    var onViewDressListener: OnViewDressListener =
        OnViewDressListener { dress: Dress -> }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is DressViewHolder) {
            holder.bind(items[position])
        } else if (holder is EndViewHolder) {
            holder.bind(true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        if (viewType == 0){
        return DressViewHolder(
            LayoutInflater.from(context).inflate(
                if (!isSizeSmall)R.layout.item_dress_picture else R.layout.item_dress_picture_small,
                parent,
                false
            )
        )
        } else{
            return EndViewHolder(
                LayoutInflater.from(context).inflate(
                   R.layout.item_dress_picture_small,
                    parent,
                    false
                )
            )
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (isSizeSmall && position == 9 && items.size >= 12) 1 else 0
    }
    override fun getItemCount(): Int {
        if(isSizeSmall) {
            return if (items.size < 12) items.size else 12
        } else{
            return items.size
        }
    }


    inner class DressViewHolder(itemView: View) : BaseViewHolder<Dress>(itemView) {

        override fun bind(dress: Dress) {
            loadPicture(itemView.dressPhoto, dress.fileName, itemView.progress)
            itemView.setOnClickListener { onViewDressListener.doAction(dress) }
        }

    }
    inner class EndViewHolder(itemView: View) : BaseViewHolder<Boolean>(itemView) {

        override fun bind(bool : Boolean) {
            itemView.dressPhoto.setImageResource(R.drawable.dots)
        }
    }
    fun loadPicture(
        imageView: ImageView,
        fileName: String,
        progress: ProgressBar
    ) {
        if (!fileName.equals("")) {
            progress.visibility = View.VISIBLE
            com.hast.norvialle.utils.loadPictureThumbnail(context, fileName).subscribeBy(
                onNext = {
                    imageView.setImageBitmap(it)
                },
                onComplete = {progress.visibility = View.GONE},
                onError={progress.visibility = View.GONE
                it.printStackTrace()}
            )

        }

    }


    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }



  class OnViewDressListener(private val onViewDressListener: (dress : Dress) -> Unit) {
        fun doAction(dress : Dress) {
            onViewDressListener(dress)
        }
    }
}


