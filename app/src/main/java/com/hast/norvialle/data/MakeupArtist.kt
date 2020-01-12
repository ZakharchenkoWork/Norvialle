package com.hast.norvialle.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 02.12.2019.
 */
@Entity
class MakeupArtist(name: String, defaultPrice: Int, phone: String) : BaseObservable(),
    Serializable {
    @PrimaryKey
    var id = ""

    @Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    var defaultPrice: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.defaultPrice)
        }

    @Bindable
    var phone: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.phone)
        }

    init {
        this.name = name
        this.defaultPrice = defaultPrice
        this.phone = phone
    }
}
