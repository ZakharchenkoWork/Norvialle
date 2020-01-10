package com.hast.norvialle.domain

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.hast.norvialle.domain.base.BaseViewModel
import com.hast.norvialle.data.Contact
import com.hast.norvialle.data.PhoneContact

/**
 * Created by Konstantyn Zakharchenko on 09.01.2020.
 */
class AddContactViewModel(application : Application) : BaseViewModel(application){
   val contactLiveData: MutableLiveData<Contact> = MutableLiveData()

    fun setContact(contact: Contact?) {
        if (contact != null){
            contactLiveData.value = contact
        } else{
            contactLiveData.value = Contact("", "", "")
        }


    }

    fun setPhoneContact(phoneContact: PhoneContact) {
        contactLiveData.value?.name = phoneContact.name
        contactLiveData.value?.phone = phoneContact.phone
    }

    fun save() {
        MainPresenter.addContact(contactLiveData.value)
    }

    fun setLink(link : String) {
        contactLiveData.value?.link = link
    }

}
