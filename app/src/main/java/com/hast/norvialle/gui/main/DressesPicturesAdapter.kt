package com.hast.norvialle.gui.main

import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.hast.norvialle.R
import com.hast.norvialle.data.Dress
import com.hast.norvialle.data.MakeupArtist
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.item_dress.view.*
import java.io.File
import java.io.FileInputStream


class DressesPicturesAdapter(val items: ArrayList<Dress>, private val context: Context) :
    RecyclerView.Adapter<DressesPicturesAdapter.BaseViewHolder<*>>() {

    var onViewDressListener: OnViewDressListener =
        OnViewDressListener{ dress : Dress -> }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        (holder as DressViewHolder).bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return DressViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dress_picture,
                parent,
                false
            )
        )
    }



    override fun getItemCount(): Int {
        return items.size
    }


    inner class DressViewHolder(itemView: View) : BaseViewHolder<Dress>(itemView) {

        override fun bind(dress: Dress) {
            loadPicture(itemView.dressPhoto, dress.fileName, itemView.progress)
            itemView.setOnClickListener { onViewDressListener.doAction(dress) }
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


