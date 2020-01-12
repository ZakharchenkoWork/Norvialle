package com.hast.norvialle.presenataion.base.assistent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.hast.norvialle.R
import com.hast.norvialle.databinding.ActivityAddAssistantBinding
import com.hast.norvialle.domain.AddAssistantViewModel
import com.hast.norvialle.presenataion.base.BaseActivity
import com.hast.norvialle.presenataion.base.openContactsList
import com.hast.norvialle.presenataion.utils.dialogs.priceInputDialog
import com.hast.norvialle.utils.getIntValue
import kotlinx.android.synthetic.main.activity_add_makeup_artist.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class AddAssistantActivity : BaseActivity() {
    companion object {
        const val EDIT: Int = 1
        const val DATA_TYPE: String = "ASSISTANT"
        const val PICK_CONTACT: Int = 678
    }

    private lateinit var viewModel: AddAssistantViewModel
    private lateinit var binding: ActivityAddAssistantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_assistant)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(AddAssistantViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.setAssistant(getIntentData(DATA_TYPE))

        contactsList.setOnClickListener { openContactsList(PICK_CONTACT) }
        price.setOnClickListener {
            priceInputDialog(
                this,
                R.string.makeupPrice,
                price.getIntValue().toFloat()
            ) {
                viewModel.setPrice(getIntValue(it))
                price.postDelayed({
                    name.validationCheck()
                    phone.validationCheck()
                }, 100)
            }
        }
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                try {
                    checkField(name)
                    checkField(phone)
                    checkField(price)

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
        return super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getMenuRes(): Int {
        return R.menu.menu_save
    }

    override fun getMenuTitleRes(): Int {
        return R.string.adding_assistant
    }
}