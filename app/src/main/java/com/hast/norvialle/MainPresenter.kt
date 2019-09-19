package com.hast.norvialle

/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
class MainPresenter {
    val events: ArrayList<EventList> = ArrayList()

    init {
        val clients: ArrayList<Event> = ArrayList()
        clients.add(Event("Нюша", "16:30"))
        clients.add(Event("Крошик", "17:30"))
        clients.add(Event("Пандочка", "18:30"))
        clients.add(Event("Виктория Константиновна", "19:30"))
        clients.add(Event("Буба", "20:30"))
        clients.add(Event("АмНям", "21:30"))
        events.add(EventList("21.08.19", clients))
        events.add(EventList("22.08.19", clients))
        events.add(EventList("23.08.19", clients))
        events.add(EventList("24.08.19", clients))
        events.add(EventList("25.08.19", clients))
        events.add(EventList("26.08.19", clients))
        events.add(EventList("27.08.19", clients))

    }

}