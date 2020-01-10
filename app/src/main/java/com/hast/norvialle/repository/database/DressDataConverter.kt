package com.hast.norvialle.repository.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hast.norvialle.data.Dress
import java.lang.reflect.Type


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class DressDataConverter {

    @TypeConverter
    fun fromCountryLangList(countryLang: ArrayList<Dress>): String {
        if (countryLang.equals("")) {
            return "[]"
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<ArrayList<Dress>>(){}.getType()
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String): ArrayList<Dress> {
        if (countryLangString.equals("")) {
            return ArrayList()
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<ArrayList<Dress>>() {}.getType()
        return gson.fromJson(countryLangString, type)
    }
}