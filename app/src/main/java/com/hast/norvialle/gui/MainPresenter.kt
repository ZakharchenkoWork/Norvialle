package com.hast.norvialle.gui

import com.hast.norvialle.App
import com.hast.norvialle.data.Event
import com.hast.norvialle.data.MakeupArtist
import com.hast.norvialle.data.Studio
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
object MainPresenter {



    var events: ArrayList<Event> = ArrayList()
    var studios: ArrayList<Studio> = ArrayList()
    var makupArtists: ArrayList<MakeupArtist> = ArrayList()

fun start(){

    events = java.util.ArrayList(App.db.eventDao().getAll())
    studios = java.util.ArrayList(App.db.studioDao().getAll())
    makupArtists = java.util.ArrayList(App.db.makeupDao().getAll())

    sort()
}
    fun sort() {
        events.sortBy { it.time }
    }

    fun addEvent(event: Event) {
        if (event.id.equals("")) {
            event.id = UUID.randomUUID().toString()
            events.add(event)
            App.db.eventDao().insert(event)
            sort()
        } else {

            for ((index, oldEvent) in events.withIndex()) {
                if (oldEvent.id.equals(event.id)) {
                    events.set(index, event)
                    sort()
                    App.db.eventDao().update(event)
                    return
                }
            }
        }


    }

    fun delete(event: Event) {
        App.db.eventDao().delete(event)
        events.remove(event)
    }
    fun deleteStudio(studio: Studio) {
        App.db.studioDao().delete(studio)
        studios.remove(studio)
    }
    fun addStudio(studio: Studio) {
        if (studio.id.equals("")) {
            studio.id = UUID.randomUUID().toString()

            App.db.studioDao().insert(studio)
            studios.add(studio)
        } else{
            App.db.studioDao().update(studio)
            for ((index, oldStudio) in studios.withIndex()) {
                if(oldStudio.id.equals(studio.id)){
                    studios.set(index, studio)
                    return
                }
            }
            studios.add(studio)
        }
    }

    fun addMakeupArtist(makupArtist: MakeupArtist){
        if (makupArtist.id.equals("")) {
            makupArtist.id = UUID.randomUUID().toString()

            App.db.makeupDao().insert(makupArtist)
            makupArtists.add(makupArtist)
        } else{
            App.db.makeupDao().update(makupArtist)
            for ((index, oldMakupArtists) in makupArtists.withIndex()) {
                if(oldMakupArtists.id.equals(makupArtist.id)){
                    makupArtists.set(index, makupArtist)
                    return
                }
            }
            makupArtists.add(makupArtist)
        }
    }
    fun deleteMakeupArtist(makupArtist: MakeupArtist) {
        App.db.makeupDao().delete(makupArtist)
        makupArtists.remove(makupArtist)
    }
}