package com.hast.norvialle.gui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.hast.norvialle.data.PhotoRoom
import com.hast.norvialle.data.Studio

/**
 * Created by Konstantyn Zakharchenko on 07.12.2019.
 */
abstract class BaseAdapter<DataType> (private val allItems: ArrayList<DataType>, protected val context: Context) :
    RecyclerView.Adapter<BaseAdapter.BaseViewHolder<*>>() {
    var onEditistener: ((data: DataType)->Unit)? = null
    var onDeleteListener: ((data: DataType)->Unit)? = null

    var onPickListener: ((data: DataType)->Unit)? = null
    var onPickListenerWithOptional: ((data: DataType, optional : Any)->Unit)? = null
    var isForResult = false
    val items: ArrayList<DataType> = ArrayList()

    init{
        items.addAll(allItems)
    }
    abstract override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int)



    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*>

    override fun getItemCount(): Int {
        return items.size
    }


    fun open2gis(link: String) {
        val uri = Uri.parse(link.replace("http://", "dgis://"))

        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage("ru.dublgis.dgismobile") // Если не планируете работать с публичной бета-версией, эту строчку надо указать

        context.startActivity(intent)
    }

    fun dial(phone: String) {

        context.startActivity(
            Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", phone, null)
            )
        )
    }

    fun openInsta(link : String){
        val uri = Uri.parse(link)
        val likeIng = Intent(Intent.ACTION_VIEW, uri)
        likeIng.setPackage("com.instagram.android")
        try {
            context.startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, uri)
            )
        }
    }
abstract fun isMatchingFilter(data:DataType, filterText : String) : Boolean



    fun filterBy(filterText : String) {
        if (!filterText.equals("")) {
            items.clear()
            for (item in allItems) {
                if (isMatchingFilter(item, filterText)) {
                    items.add(item)
                }

            }
        } else{
            items.clear()
            items.addAll(allItems)
        }
        notifyDataSetChanged()
    }


    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    fun contains(text: String, filter:String) : Boolean{
        return text.toLowerCase().contains(filter.toLowerCase())
    }

    fun checkPhone(number: String, filter:String) : Boolean{
        return preparePhone(number).contains(preparePhone(filter))

    }
    fun preparePhone(phone : String) : String{
        return phone.replace(" ", "").replace("(", "").replace(")", "").replace("-", "").replace("+", "").trim()
    }
}


