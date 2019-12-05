package com.hast.norvialle.gui.dresses

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


class DressesAdapter(val items: ArrayList<Dress>, private val context: Context) :
    RecyclerView.Adapter<DressesAdapter.BaseViewHolder<*>>() {

    var onEditDressListener: OnEditDressListener =
        OnEditDressListener {}
    var onDeleteDressListener: OnEditDressListener =
        OnEditDressListener {}

    var onViewDressListener: OnViewDressListener =
        OnViewDressListener{ dress : Dress -> }

val pickedDresses : ArrayList<Dress> = ArrayList()
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        (holder as DressViewHolder).bind(items[position])
    }

    var isForResult = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return DressViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dress,
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
            if (!dress.comment.equals("")) {
                itemView.comment.setText(dress.comment)
                itemView.comment.visibility = View.VISIBLE
            } else{
                itemView.comment.visibility = View.INVISIBLE
            }
            if (dress.price != 0) {
                itemView.dressPrice.setText(""+dress.price)
                itemView.dressPrice.visibility = View.VISIBLE
            } else{
                itemView.dressPrice.visibility = View.INVISIBLE
            }

            loadPicture(itemView.dressPhoto, dress.fileName, itemView.progress)

            if(!isForResult) {
                itemView.foregroundLayout.setOnClickListener { itemView.foregroundLayout.visibility = View.GONE }
                itemView.backgroundLayout.setOnClickListener { itemView.foregroundLayout.visibility = View.VISIBLE }
                itemView.edit.setOnClickListener {
                    onEditDressListener.doAction(dress)
                }
                itemView.delete.setOnClickListener { onDeleteDressListener.doAction(dress) }
                itemView.view.setOnClickListener { onViewDressListener.doAction(dress) }
                itemView.dressPickedView.visibility = View.GONE
            } else{
                itemView.setOnClickListener {
                    if (!pickedDresses.contains(dress)) {
                        pickedDresses.add(dress)
                        itemView.dressPickedView.visibility = View.VISIBLE
                    } else{
                        pickedDresses.remove(dress)
                        itemView.dressPickedView.visibility = View.GONE
                    }

                }


            }
        }
    }


    fun loadPicture(
        imageView: ImageView,
        fileName: String,
        progress: ProgressBar
    ) {
        if (!fileName.equals("")) {
            progress.visibility = View.VISIBLE
            com.hast.norvialle.utils.loadPicture(context, fileName).subscribeBy(
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


    class OnEditDressListener(private val onEditDressListener: (dress : Dress) -> Unit) {
        fun doAction(dress : Dress) {
            onEditDressListener(dress)
        }
    }

  class OnViewDressListener(private val onViewDressListener: (dress : Dress) -> Unit) {
        fun doAction(dress : Dress) {
            onViewDressListener(dress)
        }
    }
}


