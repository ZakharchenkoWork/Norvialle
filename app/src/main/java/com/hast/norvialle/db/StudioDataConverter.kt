package com.hast.norvialle.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hast.norvialle.data.PhotoRoom
import java.lang.reflect.Type


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class StudioDataConverter {

    @TypeConverter
    fun fromCountryLangList(countryLang: List<PhotoRoom?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<PhotoRoom?>?>() {}.getType()
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String?): List<PhotoRoom>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<PhotoRoom?>?>() {}.getType()
        return gson.fromJson(countryLangString, type)
    }
}