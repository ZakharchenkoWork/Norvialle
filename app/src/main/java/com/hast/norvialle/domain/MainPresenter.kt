package com.hast.norvialle.domain

import com.hast.norvialle.App
import com.hast.norvialle.data.*
import com.hast.norvialle.repository.database.AppDatabase
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
object MainPresenter {


    var events: ArrayList<Event> = ArrayList()
    var studios: ArrayList<Studio> = ArrayList()
    var makeupArtists: ArrayList<MakeupArtist> = ArrayList()
    var assistants: ArrayList<Assistant> = ArrayList()
    var dresses: ArrayList<Dress> = ArrayList()
    var contacts: ArrayList<Contact> = ArrayList()
    var settings: Settings = Settings()

    fun getEventsList(): ArrayList<Event> {
        if (events.isEmpty()) {
            events = java.util.ArrayList(App.db.eventDao().getAll())
        }
        return events
    }
    fun start() {

        events = java.util.ArrayList(App.db.eventDao().getAll())
        studios = java.util.ArrayList(App.db.studioDao().getAll())
        makeupArtists = java.util.ArrayList(App.db.makeupDao().getAll())
        dresses = java.util.ArrayList(App.db.dressDao().getAll())
        contacts = java.util.ArrayList(App.db.contactsDao().getAll())

        if(AppDatabase.migrateFrom4to5){
            settings.setDefault()
            App.db.settingsDao().insert(settings)
        }
        settings = App.db.settingsDao().get()
        sort()
        if (AppDatabase.migrationFrom6to7){
            for (event in events) {
                event.additionalId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
                App.db.eventDao().update(event)
            }
        }
    }

    fun sort() {
        events.sortBy { it.time }
    }

    fun addEvent(event: Event) {
        if (event.id.equals("")) {
            event.id = ""+(Math.random() * 1000000).toInt()
            event.additionalId =  (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
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
        } else {
            App.db.studioDao().update(studio)
            for ((index, oldStudio) in studios.withIndex()) {
                if (oldStudio.id.equals(studio.id)) {
                    studios.set(index, studio)
                    return
                }
            }
            studios.add(studio)
        }
    }

    fun addMakeupArtist(makeupArtist: MakeupArtist?) {
        if(makeupArtist == null){
            return
        }
        if (makeupArtist.id.equals("")) {
            makeupArtist.id = UUID.randomUUID().toString()

            App.db.makeupDao().insert(makeupArtist)
            makeupArtists.add(makeupArtist)
        } else {
            App.db.makeupDao().update(makeupArtist)
            for ((index, oldmakeupArtists) in makeupArtists.withIndex()) {
                if (oldmakeupArtists.id.equals(makeupArtist.id)) {
                    makeupArtists.set(index, makeupArtist)
                    return
                }
            }
            makeupArtists.add(makeupArtist)
        }
    }

    fun addContact(contact: Contact?) {
        if(contact == null){
            return
        }
        if (contact.id.equals("")) {
            contact.id = UUID.randomUUID().toString()

            App.db.contactsDao().insert(contact)
            contacts.add(contact)
        } else {
            App.db.contactsDao().update(contact)
            for ((index, oldContact) in contacts.withIndex()) {
                if (oldContact.id.equals(contact.id)) {
                    contacts.set(index, contact)
                    return
                }
            }
            contacts.add(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        App.db.contactsDao().delete(contact)
        contacts.remove(contact)
    }

    fun deleteMakeupArtist(makeupArtist: MakeupArtist) {
        App.db.makeupDao().delete(makeupArtist)
        makeupArtists.remove(makeupArtist)
    }

    fun addDress(dress: Dress) {
        if (dress.id.equals("")) {
            dress.id = UUID.randomUUID().toString()

            App.db.dressDao().insert(dress)
            dresses.add(dress)
        } else {
            App.db.dressDao().update(dress)
            for ((index, oldDress) in dresses.withIndex()) {
                if (oldDress.id.equals(dress.id)) {
                    dresses.set(index, dress)
                    return
                }
            }
            dresses.add(dress)
        }
    }

    fun deleteDress(dress: Dress) {
        App.db.dressDao().delete(dress)
        if (dresses.contains(dress)) {
            dresses.remove(dress)
        } else {
            for (dressFromList in dresses) {
                if (dressFromList.fileName.equals(dress.fileName)) {
                    dresses.remove(dressFromList)
                    return
                }
            }
        }
    }

    fun saveSettings(settings: Settings) {
        App.db.settingsDao().update(settings)
        MainPresenter.settings = settings
    }

    fun addAssistent(assistant: Assistant) {
        if (assistant.id.equals("")) {
            assistant.id = UUID.randomUUID().toString()

            App.db.assistentDao().insert(assistant)
            assistants.add(assistant)
        } else {
            App.db.assistentDao().update(assistant)
            for ((index, oldAssistent) in assistants.withIndex()) {
                if (oldAssistent.id.equals(assistant.id)) {
                    assistants.set(index, assistant)
                    return
                }
            }
            assistants.add(assistant)
        }
    }

    fun deleteAssistent(assistant: Assistant) {
        App.db.assistentDao().delete(assistant)
        assistants.remove(assistant)
    }


}