package com.hast.norvialle.gui.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import com.hast.norvialle.R
import com.hast.norvialle.data.AuthData
import com.hast.norvialle.gui.BaseActivity
import com.hast.norvialle.gui.dialogs.SimpleDialog
import com.hast.norvialle.gui.main.MainActivity
import com.hast.norvialle.model.ApiImpl
import com.hast.norvialle.utils.SETTING_AUTO_LOGIN
import com.hast.norvialle.utils.SETTING_FINGERPRINT
import com.hast.norvialle.utils.biometric.BiometricCallback
import com.hast.norvialle.utils.biometric.BiometricManager
import com.hast.norvialle.utils.data_validation.DataValidationController
import com.hast.norvialle.utils.data_validation.Validatable
import com.hast.norvialle.utils.getEmailWatcher
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_auth.*


/**
 * Created by Konstantyn Zakharchenko on 17.12.2019.
 */
class AuthActivity : BaseActivity(), BiometricCallback {
    val loginValidation = DataValidationController()
    val registerValidation = DataValidationController()
    var biometricManager: BiometricManager? = null
    override fun getMenuRes(): Int {
        return NO_VIEW
    }

    override fun getMenuTitleRes(): Int {
        return NO_VIEW
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        biometricManager = BiometricManager.BiometricBuilder(this)
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setDescription(getString(R.string.biometric_description))
            .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
            .build()
        loginLayout.visibility = View.VISIBLE
        registerLayout.visibility = View.GONE
        registration.setOnClickListener {
            registerLayout.visibility = View.VISIBLE
            back.visibility = View.VISIBLE
            loginLayout.visibility = View.GONE
            forgotPassword.visibility = View.GONE
        }
        back.setOnClickListener {
            registerLayout.visibility = View.GONE
            back.visibility = View.GONE
            loginLayout.visibility = View.VISIBLE
            forgotPassword.visibility = View.VISIBLE
        }
        checkPhotograph.setOnCheckedChangeListener { a, isChecked ->
            validateCheckBoxes()
        }
        checkAssistant.setOnCheckedChangeListener { a, isChecked ->
            validateCheckBoxes()
        }

        checkMakeupArtist.setOnCheckedChangeListener { a, isChecked ->
            validateCheckBoxes()
        }

        loginValidation.add(signLogin)
        loginValidation.add(signPassword)
        loginValidation.onStateChanged = { signIn.isEnabled = it }
        signIn.setOnClickListener {

            login(
                AuthData(
                    signLogin.getText(),
                    signPassword.getText()
                )
            )
        }

        remember.setOnCheckedChangeListener { a, isChecked ->
            fingerprint.visibility = if (!isChecked) View.VISIBLE else View.GONE
        }
        fingerprint.isChecked = loadData(SETTING_FINGERPRINT)
        remember.isChecked = loadData(SETTING_AUTO_LOGIN, true)
        if (remember.isChecked){
            val authData = loadAuthData()
            if (!authData.isEmpty()){
                login(authData)
            }
        }

        if (fingerprint.isChecked) {
            biometricManager?.authenticate(this)
        }
        forgotPassword.setOnClickListener {
            SimpleDialog(this, SimpleDialog.DIALOG_TYPE.MESSAGE_AND_INPUT)
                .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .setTitle(getString(R.string.restore_password))
                .setMessage(getString(R.string.restore_password_message))
                .setOkListener {
                    //TODO: Send message to server
                }
                .build()
        }

        registerButton.setOnClickListener {
            register(AuthData(regLogin.getText().toLowerCase(), regPassword.getText()))
        }
        registerButton.isEnabled = false

        registerValidation.add(regLogin)
        registerValidation.add(regPassword)
        registerValidation.add(regConfirmPassword)
        registerValidation.add(PasswordsMatcher())
        registerValidation.add(ChecksWatcher())
        registerValidation.onStateChanged = {
            registerButton.isEnabled = it
        }
    }

    fun validateCheckBoxes() : Boolean {
        return if (!checkPhotograph.isChecked &&
        !checkAssistant.isChecked &&
        !checkMakeupArtist.isChecked){
            checkPhotograph.buttonTintList = ColorStateList.valueOf(getResolvedColor(R.color.red))
            checkAssistant.buttonTintList = ColorStateList.valueOf(getResolvedColor(R.color.red))
            checkMakeupArtist.buttonTintList = ColorStateList.valueOf(getResolvedColor(R.color.red))
            false
        } else{
            checkPhotograph.buttonTintList = ColorStateList.valueOf(getResolvedColor(R.color.gold))
            checkAssistant.buttonTintList = ColorStateList.valueOf(getResolvedColor(R.color.gold))
            checkMakeupArtist.buttonTintList = ColorStateList.valueOf(getResolvedColor(R.color.gold))
            true
        }
    }
    inner class PasswordsMatcher : Validatable{
        init{
            regPassword.onFocusChanged = {

                    validationCheck()

            }
            regConfirmPassword.onFocusChanged = {

                    validationCheck()

            }
        }
        override var onStateChanged: () -> Unit = {}
        override fun validationCheck(): Boolean {
            val equals = regPassword.getText().equals(regConfirmPassword.getText())
            if (!equals) {
                regPassword.post {
                    regPassword.setColors(false)
                    regConfirmPassword.setColors(false)
                }
            }
            return equals
        }

    }

    inner class ChecksWatcher : Validatable{

        init{
            checkPhotograph.setOnCheckedChangeListener{ v, h->
                onStateChanged()
            }
            checkMakeupArtist.setOnCheckedChangeListener{ v, h->
                onStateChanged()
            }
            checkAssistant.setOnCheckedChangeListener{ v, h->
                onStateChanged()
            }
        }
        override var onStateChanged: () -> Unit = {}
        override fun validationCheck(): Boolean {

            return validateCheckBoxes()
        }

    }

    override fun onSdkVersionNotSupported() {
        Log.i("Auth", "Not compatible version of android")
        fingerprint.visibility = View.GONE
    }

    override fun onBiometricAuthenticationNotSupported() {
        Log.i("Auth", "Not supported")
        fingerprint.visibility = View.GONE
    }

    override fun onBiometricAuthenticationNotAvailable() {
        Log.i("Auth", "not available")
        fingerprint.visibility = View.GONE
    }

    override fun onBiometricAuthenticationPermissionNotGranted() {
        Log.i("Auth", "no permission")
        fingerprint.visibility = View.GONE
    }

    override fun onBiometricAuthenticationInternalError(error: String?) {
        Log.i("Auth",  error)
    }

    override fun onAuthenticationFailed() {
        //        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    override fun onAuthenticationCancelled() {
        Log.i("Auth", "Canceled")
        biometricManager?.cancelAuthentication()
    }

    override fun onAuthenticationSuccessful() {

        login(loadAuthData())
    }

    override fun onAuthenticationHelp(
        helpCode: Int,
        helpString: CharSequence?
    ) { //        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    override fun onAuthenticationError(
        errorCode: Int,
        errString: CharSequence?
    ) { //        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }

fun register(authData: AuthData){
    ApiImpl().registration(authData).subscribeBy (onNext = {
        Log.i("rest", it.userId)
    }, onError = {
        it.printStackTrace()
    })

}

    fun login(authData: AuthData) {
        saveData(SETTING_AUTO_LOGIN, remember.isChecked)
        saveData(SETTING_FINGERPRINT, fingerprint.isChecked)
       saveAuthData(authData)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}