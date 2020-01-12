package com.hast.norvialle.presenataion.base.contacts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.hast.norvialle.R
import com.hast.norvialle.databinding.ActivityAddContactBinding
import com.hast.norvialle.domain.AddContactViewModel
import com.hast.norvialle.presenataion.base.BaseActivity
import com.hast.norvialle.presenataion.base.openContactsList
import kotlinx.android.synthetic.main.activity_add_contact.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class AddContactActivity : BaseActivity() {

    companion object {
        const val EDIT: Int = 111
        const val DATA_TYPE = "CONTACT"
        const val PICK_CONTACT: Int = 911
    }

    private lateinit var viewModel: AddContactViewModel
    private lateinit var binding: ActivityAddContactBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(AddContactViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.setContact(getIntentData(DATA_TYPE))

        paste.setOnClickListener {

            val pastedLinkedInLink = pasteLinkedInLink()
            if (pastedLinkedInLink != null) {
                viewModel.setLink(pastedLinkedInLink)
            } else {
                fastToast(getString(R.string.insta_hint))
            }
        }
        contactsList.setOnClickListener { openContactsList(PICK_CONTACT) }
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                try {
                    checkField(name)
                    checkField(phone)

                    viewModel.save()
                    finishWithOkResult()
                } catch (dataCheckFailed: InvalidSavingData) {
                    fastToast(dataCheckFailed)
                    return true
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (isResultDataOk(resultCode, requestCode, PICK_CONTACT)) {
            data?.let { viewModel.setPhoneContact(extractContact(data)) }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getMenuRes(): Int {
        return R.menu.menu_save
    }

    override fun getMenuTitleRes(): Int {
        return R.string.adding_contact
    }
}