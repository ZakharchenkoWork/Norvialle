package com.hast.norvialle.gui.contacts


import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.Contact
import com.hast.norvialle.gui.BaseFragment

import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.utils.getSearchTextWatcher
import com.hast.norvialle.utils.showDeleteDialog
import kotlinx.android.synthetic.main.activity_search_list.*
import kotlinx.android.synthetic.main.activity_search_list.view.*

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class ContactsListFragment : BaseFragment() {

    var onResultListener: ((contact: Contact)->Unit)? = null
    var isSearchShown = false
    companion object {
        fun newInstance(onResultListener: ((contact: Contact)->Unit)? = null): BaseFragment {
            val fragment = ContactsListFragment()
            fragment.onResultListener = onResultListener
            return fragment
        }
    }

    override fun getContentId(): Int {
       return R.layout.activity_search_list
    }

    override fun getMenuId(): Int {
      return R.menu.menu_search_plus
    }

    override fun getMenuTitleId(): Int {
        return R.string.contacts
    }

    override fun onCreateView(view: View): View {
        view.list.layoutManager = LinearLayoutManager(context)
        return view
    }



    override fun onResume() {
        super.onResume()
        context?.let {
            prepareAdapter(it)
        }
    }
    fun prepareAdapter(context: Context) {

            val adapter = ContactsAdapter(MainPresenter.contacts, context)
        val isForResult = onResultListener != null
            adapter.isForResult =isForResult
            root.list.adapter = adapter
            if (!isForResult) {
                adapter.onEditistener = {
                    openEditor(it)
                }
                adapter.onDeleteListener = {
                    showDeleteDialog(context, R.string.delete_dialog_contact){
                        MainPresenter.deleteContact(it)
                        prepareAdapter(context)
                    }

                }
            } else {
                adapter.onPickListener = {
                        contact : Contact ->
                    onResultListener?.let { it.invoke(contact)
                        fragmentManager?.popBackStack()
                    }
                }
            }
            root.searchFilter.addTextChangedListener(getSearchTextWatcher { adapter.filterBy(it) })
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.plus -> {
                openEditor(Contact("","",""))
            }
            R.id.search-> {
            isSearchShown = !isSearchShown
            searchFilter.visibility = if (isSearchShown) View.VISIBLE else View.GONE
        }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun openEditor(contact : Contact) {
        val intent = Intent(context, AddContactActivity::class.java)
        intent.putExtra(AddContactActivity.DATA_TYPE, contact)
        startActivityForResult(intent, AddContactActivity.EDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        context?.let { prepareAdapter(it) }
        super.onActivityResult(requestCode, resultCode, data)
    }
}