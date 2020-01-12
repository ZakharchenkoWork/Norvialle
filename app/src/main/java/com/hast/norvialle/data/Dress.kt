package com.hast.norvialle.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 03.12.2019.
 */

@Entity
class Dress (fileName : String, comment : String = "", price : Int = 0) :BaseObservable(), Serializable{
@PrimaryKey
var id :String= ""


    @Bindable
    var fileName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.fileName)
        }

    @Bindable
    var comment : String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.comment)
        }
    @Bindable
    var price : Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.price)
        }
    override fun equals(other: Any?): Boolean {
        return if (other is Dress && other.fileName.equals(fileName))true else super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + comment.hashCode()
        result = 31 * result + price
        return result
    }

    init {
        this.fileName = fileName
        this.comment = comment
        this.price = price
    }
}