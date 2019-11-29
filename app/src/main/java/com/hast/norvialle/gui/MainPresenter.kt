package com.hast.norvialle.gui

import com.hast.norvialle.App
import com.hast.norvialle.Event
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
object MainPresenter {


    var events: ArrayList<Event> = ArrayList()
    val ONE_HOUR: Long = 60 * 60 * 1000


fun start(){

    events = java.util.ArrayList(App.db.eventDao().getAll())

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

}