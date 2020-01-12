package com.hast.norvialle.utils.bindings

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.hast.norvialle.domain.AddDressViewModel
import com.hast.norvialle.utils.bindings.extensions.getParentActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import java.io.File

/**
 * Created by Konstantyn Zakharchenko on 29.12.2019.
 */
@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value ->
            value?.let { view.visibility = if (it) View.VISIBLE else View.GONE }
            }
        )
    }
}

@BindingAdapter(value = ["image", "loadingCallback", "cache"], requireAll = false)
fun loadImage(imageView: ImageView, imageName: String, loadingCallback : Callback, cache : Boolean = true): String {
    if (imageName == "") {
        return "false"
    }
    val cw = ContextWrapper(imageView.context.applicationContext)
    val directory: File = cw.getDir("dresses", Context.MODE_PRIVATE)
    val picasso = Picasso.with(imageView.context).load(File(directory, imageName))
    if (!cache) {
        picasso.memoryPolicy(MemoryPolicy.NO_CACHE)
    }
        picasso.into(imageView, loadingCallback)
    return "true"
}
