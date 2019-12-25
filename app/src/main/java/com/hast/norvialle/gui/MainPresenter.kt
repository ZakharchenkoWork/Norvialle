package com.hast.norvialle.gui

import com.hast.norvialle.App
import com.hast.norvialle.data.*
import com.hast.norvialle.data.server.ACTION_TYPE
import com.hast.norvialle.data.server.DATA_TYPE
import com.hast.norvialle.data.server.UpdateData
import com.hast.norvialle.db.AppDatabase
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
object MainPresenter {


    var events: ArrayList<Event> = ArrayList()
    var studios: ArrayList<Studio> = ArrayList()
    var makupArtists: ArrayList<MakeupArtist> = ArrayList()
    var assistants: ArrayList<Assistant> = ArrayList()
    var dresses: ArrayList<Dress> = ArrayList()
    var contacts: ArrayList<Contact> = ArrayList()
    var localUpdates: ArrayList<UpdateData> = ArrayList()
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
        makupArtists = java.util.ArrayList(App.db.makeupDao().getAll())
        dresses = java.util.ArrayList(App.db.dressDao().getAll())
        contacts = java.util.ArrayList(App.db.contactsDao().getAll())
        assistants = java.util.ArrayList(App.db.assistentDao().getAll())
        localUpdates = java.util.ArrayList(App.db.updatesDao().getAll())
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
            addUpdate(UpdateData(dataType = DATA_TYPE.EVENT, actionType = ACTION_TYPE.ADD, itemId = event.id))
        } else {

            for ((index, oldEvent) in events.withIndex()) {
                if (oldEvent.id.equals(event.id)) {
                    events.set(index, event)
                    sort()
                    App.db.eventDao().update(event)
                    addUpdate(UpdateData(dataType = DATA_TYPE.EVENT, actionType = ACTION_TYPE.EDIT, itemId = event.id))
                    return
                }
            }
        }


    }
    fun addUpdate(updateData: UpdateData){
        App.db.updatesDao().insert(updateData)
        localUpdates.add(updateData)

    }

    fun delete(event: Event) {
        App.db.eventDao().delete(event)
        events.remove(event)
        addUpdate(UpdateData(dataType = DATA_TYPE.EVENT, actionType = ACTION_TYPE.REMOVE, itemId = event.id))
    }

    fun deleteStudio(studio: Studio) {
        App.db.studioDao().delete(studio)
        studios.remove(studio)
        addUpdate(UpdateData(dataType = DATA_TYPE.STUDIO, actionType = ACTION_TYPE.REMOVE, itemId = studio.id))
    }

    fun addStudio(studio: Studio) {
        if (studio.id.equals("")) {
            studio.id = UUID.randomUUID().toString()

            App.db.studioDao().insert(studio)
            studios.add(studio)
            addUpdate(UpdateData(dataType = DATA_TYPE.STUDIO, actionType = ACTION_TYPE.ADD, itemId = studio.id))
        } else {
            App.db.studioDao().update(studio)
            for ((index, oldStudio) in studios.withIndex()) {
                if (oldStudio.id.equals(studio.id)) {
                    studios.set(index, studio)

                    return
                }
            }
            addUpdate(UpdateData(dataType = DATA_TYPE.STUDIO, actionType = ACTION_TYPE.EDIT, itemId = studio.id))
            studios.add(studio)
        }
    }

    fun addMakeupArtist(makupArtist: MakeupArtist) {
        if (makupArtist.id.equals("")) {
            makupArtist.id = UUID.randomUUID().toString()

            App.db.makeupDao().insert(makupArtist)
            makupArtists.add(makupArtist)
            addUpdate(UpdateData(dataType = DATA_TYPE.MAKEUP_ARTIST, actionType = ACTION_TYPE.ADD, itemId = makupArtist.id))
        } else {
            App.db.makeupDao().update(makupArtist)
            for ((index, oldMakupArtists) in makupArtists.withIndex()) {
                if (oldMakupArtists.id.equals(makupArtist.id)) {
                    makupArtists.set(index, makupArtist)
                    return
                }
            }
            addUpdate(UpdateData(dataType = DATA_TYPE.MAKEUP_ARTIST, actionType = ACTION_TYPE.EDIT, itemId = makupArtist.id))
            makupArtists.add(makupArtist)
        }
    }

    fun addContact(contact: Contact) {
        if (contact.id.equals("")) {
            contact.id = UUID.randomUUID().toString()

            App.db.contactsDao().insert(contact)
            contacts.add(contact)
            addUpdate(UpdateData(dataType = DATA_TYPE.CONTACT, actionType = ACTION_TYPE.ADD, itemId = contact.id))
        } else {
            App.db.contactsDao().update(contact)
            for ((index, oldContact) in contacts.withIndex()) {
                if (oldContact.id.equals(contact.id)) {
                    contacts.set(index, contact)
                    return
                }
            }
            addUpdate(UpdateData(dataType = DATA_TYPE.CONTACT, actionType = ACTION_TYPE.EDIT, itemId = contact.id))
            contacts.add(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        App.db.contactsDao().delete(contact)
        contacts.remove(contact)
        addUpdate(UpdateData(dataType = DATA_TYPE.CONTACT, actionType = ACTION_TYPE.REMOVE, itemId = contact.id))
    }

    fun deleteMakeupArtist(makupArtist: MakeupArtist) {
        App.db.makeupDao().delete(makupArtist)
        makupArtists.remove(makupArtist)
        addUpdate(UpdateData(dataType = DATA_TYPE.MAKEUP_ARTIST, actionType = ACTION_TYPE.REMOVE, itemId = makupArtist.id))
    }

    fun addDress(dress: Dress) {
        if (dress.id.equals("")) {
            dress.id = UUID.randomUUID().toString()

            App.db.dressDao().insert(dress)
            dresses.add(dress)

            addUpdate(UpdateData(dataType = DATA_TYPE.DRESS, actionType = ACTION_TYPE.ADD, itemId = dress.id))
        } else {
            App.db.dressDao().update(dress)
            for ((index, oldDress) in dresses.withIndex()) {
                if (oldDress.id.equals(dress.id)) {
                    dresses.set(index, dress)
                    return
                }
            }
            dresses.add(dress)
            addUpdate(UpdateData(dataType = DATA_TYPE.DRESS, actionType = ACTION_TYPE.EDIT, itemId = dress.id))
        }
    }

    fun deleteDress(dress: Dress) {
        App.db.dressDao().delete(dress)
        addUpdate(UpdateData(dataType = DATA_TYPE.DRESS, actionType = ACTION_TYPE.REMOVE, itemId = dress.id))
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
        this.settings = settings
    }

    fun addAssistent(assistant: Assistant) {
        if (assistant.id.equals("")) {
            assistant.id = UUID.randomUUID().toString()

            App.db.assistentDao().insert(assistant)
            assistants.add(assistant)
            addUpdate(UpdateData(dataType = DATA_TYPE.ASSISTANT, actionType = ACTION_TYPE.ADD, itemId = assistant.id))
        } else {
            App.db.assistentDao().update(assistant)
            for ((index, oldAssistent) in assistants.withIndex()) {
                if (oldAssistent.id.equals(assistant.id)) {
                    assistants.set(index, assistant)
                    return
                }
            }
            assistants.add(assistant)
            addUpdate(UpdateData(dataType = DATA_TYPE.ASSISTANT, actionType = ACTION_TYPE.EDIT, itemId = assistant.id))
        }
    }

    fun deleteAssistent(assistant: Assistant) {
        App.db.assistentDao().delete(assistant)
        assistants.remove(assistant)
        addUpdate(UpdateData(dataType = DATA_TYPE.ASSISTANT, actionType = ACTION_TYPE.REMOVE, itemId = assistant.id))
    }

    // допустим на сервере 3 устройства.
    // Когда первый закидывает апдейт.
fun handleUpdates(serverUpdates : ArrayList<UpdateData>){
        val pendingUpdatesFromServer = ArrayList<UpdateData>()
        for (serverUpdate in serverUpdates) {
            if (!localUpdates.contains(serverUpdate)){
                pendingUpdatesFromServer.add(serverUpdate)
            }
        }

        val pendingUpdatesFromLocal = ArrayList<UpdateData>()
        for (localUpdate in localUpdates) {
            if (!serverUpdates.contains(localUpdate)){
                pendingUpdatesFromLocal.add(localUpdate)
            }
        }
        handleServerUpdates(pendingUpdatesFromServer)
        handleLocalUpdates(pendingUpdatesFromServer)
    }

    fun handleServerUpdates(pendingUpdatesFromServer: ArrayList<UpdateData>) {

    }

    fun handleLocalUpdates(pendingUpdatesFromServer: ArrayList<UpdateData>) {
        
    }


}
