package com.hast.norvialle.domain

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.hast.norvialle.data.Contact
import com.hast.norvialle.domain.base.BaseViewModel
import com.hast.norvialle.data.MakeupArtist
import com.hast.norvialle.data.PhoneContact

/**
 * Created by Konstantyn Zakharchenko on 09.01.2020.
 */
class AddMakeupArtistViewModel(application : Application) : BaseViewModel(application){
    val makeupArtistLiveData: MutableLiveData<MakeupArtist> = MutableLiveData()

    fun setMakeupArtist(makeupArtist: MakeupArtist?) {
        if (makeupArtist != null){
            makeupArtistLiveData.value = makeupArtist
        } else{
            makeupArtistLiveData.value = MakeupArtist("", 0, "")
        }


    }

    fun setPhoneContact(phoneContact: PhoneContact) {
        makeupArtistLiveData.value?.name = phoneContact.name
        makeupArtistLiveData.value?.phone = phoneContact.phone
    }

    fun save() {
        MainPresenter.addMakeupArtist(makeupArtistLiveData.value)
    }

    fun setPrice(price : Int) {
        makeupArtistLiveData.value?.defaultPrice = price
    }


}
