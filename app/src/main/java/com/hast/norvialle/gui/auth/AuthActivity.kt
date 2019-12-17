package com.hast.norvialle.gui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.gui.dialogs.SimpleDialog
import com.hast.norvialle.gui.main.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*

/**
 * Created by Konstantyn Zakharchenko on 17.12.2019.
 */
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        loginLayout.visibility = View.VISIBLE
        registerLayout.visibility = View.GONE
        registration.setOnClickListener {
            loginLayout.visibility = View.GONE
            registerLayout.visibility = View.VISIBLE
        }

        checkPhotograph.setOnCheckedChangeListener { a, isChecked ->
            if (isChecked) {
                checkAssistant.isChecked = false
                checkMakeupArtist.isChecked = false
            }
        }
        checkAssistant.setOnCheckedChangeListener { a, isChecked ->
            if (isChecked) {
                checkPhotograph.isChecked = false
                checkMakeupArtist.isChecked = false
            }
        }

        checkMakeupArtist.setOnCheckedChangeListener { a, isChecked ->
            if (isChecked) {
                checkPhotograph.isChecked = false
                checkAssistant.isChecked = false
            }
        }
        signIn.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        remember.setOnCheckedChangeListener{ a, isChecked ->
                fingerprint.visibility = if (!isChecked) View.VISIBLE else View.GONE
        }
        fingerprint.setOnCheckedChangeListener{ a, isChecked ->
            if(isChecked){
                SimpleDialog(this, SimpleDialog.DIALOG_TYPE.MESSAGE_ONLY)
                    .setMessage("Приложите палец к сенсору отпечатка пальца")
                    .setOkButtonText("Отмена")
                    .setOkListener {  }
                    .build()
            }
        }
    }
}