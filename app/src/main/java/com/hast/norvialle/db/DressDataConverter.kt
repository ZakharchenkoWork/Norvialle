package com.hast.norvialle.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hast.norvialle.data.Dress
import com.hast.norvialle.data.PhotoRoom
import java.lang.reflect.Type


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class DressDataConverter {

    @TypeConverter
    fun fromCountryLangList(countryLang: ArrayList<Dress?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<ArrayList<Dress?>?>() {}.getType()
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String?): ArrayList<Dress>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<ArrayList<Dress?>?>() {}.getType()
        return gson.fromJson(countryLangString, type)
    }
}