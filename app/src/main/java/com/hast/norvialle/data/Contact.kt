package com.hast.norvialle.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 01.12.2019.
 */


@Entity
class Contact (
    name: String,
    link: String,
    phone: String
) : BaseObservable(), Serializable {

    @PrimaryKey
    var id = ""
    @Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    var link: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.link)
        }

    @Bindable
    var phone: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.phone)
        }

    init{
        this.name = name
        this.link = link
        this.phone = phone
    }


}
