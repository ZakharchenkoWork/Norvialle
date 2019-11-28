package com.hast.norvialle.gui

import com.hast.norvialle.App
import com.hast.norvialle.Event
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
object MainPresenter {


    val events: ArrayList<Event> = ArrayList()
    val ONE_HOUR: Long = 60 * 60 * 1000

    init {
        val time: Long = System.currentTimeMillis()

        events.add(Event(UUID.randomUUID().toString(), "Нюша", time - ONE_HOUR))
        events.add(Event(UUID.randomUUID().toString(), "Крошик", time - ONE_HOUR * 5))
        events.add(Event(UUID.randomUUID().toString(), "Пандочка", time - ONE_HOUR * 10))
        events.add(Event(UUID.randomUUID().toString(), "Нюша", time + ONE_HOUR))
        events.add(Event(UUID.randomUUID().toString(), "Крошик", time + ONE_HOUR * 5))
        events.add(Event(UUID.randomUUID().toString(), "Пандочка", time + ONE_HOUR * 10))
        events.add(Event(UUID.randomUUID().toString(), "Виктория Константиновна", time + ONE_HOUR * 100))
        events.add(Event(UUID.randomUUID().toString(), "Буба", time + ONE_HOUR * 200))
        events.add(Event(UUID.randomUUID().toString(), "АмНям", time + ONE_HOUR * 3000))
        sort()

    }

    fun sort() {
        events.sortBy { it.time }
    }

    fun addEvent(event: Event) {
        if (event.id.equals("")) {
            event.id = UUID.randomUUID().toString()
            events.add(event)
            sort()
        } else {
            for ((index, oldEvent) in events.withIndex()) {
                if (oldEvent.id.equals(event.id)) {
                    events.set(index, event)
                    sort()
                    return
                }
            }
        }
    }

    fun delete(event: Event) {
        events.remove(event)
    }

}