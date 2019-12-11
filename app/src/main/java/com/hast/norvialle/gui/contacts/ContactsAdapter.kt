package com.hast.norvialle.gui.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hast.norvialle.R
import com.hast.norvialle.data.Contact
import com.hast.norvialle.gui.utils.BaseAdapter
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactsAdapter(allItems: ArrayList<Contact>, context: Context) :
    BaseAdapter<Contact>(allItems, context) {

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        (holder as ContactViewHolder).bind(items[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return ContactViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_contact,
                parent,
                false
            )
        )
    }


    inner class ContactViewHolder(itemView: View) : BaseViewHolder<Contact>(itemView) {

        override fun bind(contact: Contact) {
            itemView.name.setText(contact.name)

            if (!contact.phone.equals("")) {
                itemView.phone.setText(contact.phone)
                itemView.phone.setTextColor(context.resources.getColor(R.color.blue))
                itemView.phone.visibility = View.VISIBLE
                itemView.phone.setOnClickListener { dial(contact.phone) }
            } else {
                itemView.phone.visibility = View.INVISIBLE
                itemView.phone.setOnClickListener { }
            }

            if (!contact.link.equals("")) {
                itemView.insta.setOnClickListener {
                    openInsta(contact.link)
                }
                itemView.insta.visibility = View.VISIBLE
            } else {
                itemView.insta.visibility = View.GONE
            }

            if (!isForResult) {
                itemView.edit.setOnClickListener {
                    onEditistener?.invoke(contact)
                }
                itemView.delete.setOnClickListener {
                    onDeleteListener?.invoke(contact)
                }
                itemView.edit.visibility = View.VISIBLE
                itemView.delete.visibility = View.VISIBLE
            } else {
                itemView.edit.visibility = View.GONE
                itemView.delete.visibility = View.GONE
                itemView.setOnClickListener { onPickListener?.invoke(contact) }
            }

        }
    }

    override fun isMatchingFilter(data: Contact, filterText: String): Boolean {
        return contains(data.name, filterText) || checkPhone(data.phone, filterText)
    }
}


