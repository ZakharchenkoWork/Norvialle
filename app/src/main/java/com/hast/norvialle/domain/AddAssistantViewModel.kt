package com.hast.norvialle.domain

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.hast.norvialle.data.Assistant
import com.hast.norvialle.data.PhoneContact
import com.hast.norvialle.domain.base.BaseViewModel

/**
 * Created by Konstantyn Zakharchenko on 09.01.2020.
 */
class AddAssistantViewModel(application: Application) : BaseViewModel(application) {
    val assistantLiveData: MutableLiveData<Assistant> = MutableLiveData()

    fun setAssistant(assistant: Assistant?) {
        if (assistant != null) {
            assistantLiveData.value = assistant
        } else {
            assistantLiveData.value = Assistant("", 0, "")
        }
    }

    fun setPhoneContact(phoneContact: PhoneContact) {
        assistantLiveData.value?.name = phoneContact.name
        assistantLiveData.value?.phone = phoneContact.phone
    }

    fun save() {
        MainPresenter.addAssistant(assistantLiveData.value)
    }

    fun setPrice(price: Int) {
        assistantLiveData.value?.defaultPrice = price
    }
}
