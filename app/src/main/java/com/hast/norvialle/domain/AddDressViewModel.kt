package com.hast.norvialle.domain

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.hast.norvialle.data.Dress
import com.hast.norvialle.domain.base.BaseViewModel
import com.squareup.picasso.Callback

/**
 * Created by Konstantyn Zakharchenko on 09.01.2020.
 */
class AddDressViewModel(application: Application) : BaseViewModel(application) {
    val dressLiveData: MutableLiveData<Dress> = MutableLiveData()
    val loadingVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val loadingCallback = object : Callback {
        override fun onSuccess() {
            loadingVisibility.value = false
        }

        override fun onError() {
            loadingVisibility.value = false
        }
    }
    fun setDress(dress: Dress?) {
        if (dress != null) {
            dressLiveData.value = dress
        } else {
            dressLiveData.value = Dress("", "", 0)
        }
    }

    fun save() {
        MainPresenter.addDress(dressLiveData.value)
    }

    fun prepareLoading(pictureName : String) : String{
        if (pictureName.isNotEmpty()) {
            loadingVisibility.value = true
        }
        return pictureName
    }

    fun hasPicture(): Boolean {
        return !dressLiveData.value?.fileName.isNullOrEmpty()
    }

    fun setPicture(tempFileName: String) {
        dressLiveData.value?.fileName = tempFileName
    }

    fun getPictureFileName(): String {
        val dress = dressLiveData.value
        return dress?.fileName ?: ""
    }


}
